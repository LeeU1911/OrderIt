import { DatabaseSync } from 'node:sqlite';
import { randomBytes } from 'node:crypto';
import { broadcast } from './events.js';

export function createServices(db: DatabaseSync) {
  const sessions = new Map<string, any>();

  return {
    login(username: string, password: string) {
      const user = db.prepare('SELECT id, username, display_name, role, business_id FROM users WHERE username = ? AND password = ?').get(username, password) as any;
      if (!user) {
        throw new Error('Invalid credentials');
      }
      const token = randomBytes(16).toString('hex');
      sessions.set(token, user);
      return { user, token };
    },

    createTable(name: string) {
      if (!name) throw new Error('Name is required');
      const res = db.prepare('INSERT INTO tables (business_id, name, status, stage, guest_count, reserved, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)').run(1, name, 'available', 'available', 0, 0, new Date().toISOString());
      const table = db.prepare('SELECT * FROM tables WHERE id = ?').get(res.lastInsertRowid);
      broadcast('table.updated', table);
      return table;
    },

    deleteTable(id: number) {
      const orders = db.prepare(`SELECT id FROM orders WHERE table_id = ? AND status IN ('queued','preparing','ready','served') LIMIT 1`).all(id);
      if (orders.length > 0) {
        throw new Error('Cannot delete table with active orders');
      }
      db.prepare('DELETE FROM tables WHERE id = ?').run(id);
      broadcast('table.deleted', { id, deleted: true });
      return { success: true };
    },

    listTables() {
      const tables = db.prepare('SELECT * FROM tables ORDER BY id').all() as any[];
      const tablesWithDetails = tables.map(t => {
        // active unpaid orders
        const orders = db.prepare(`
          SELECT o.id, o.status, o.created_at, oi.prep_time_min 
          FROM orders o 
          JOIN order_items oi ON o.id = oi.order_id 
          WHERE o.table_id = ? AND o.status IN ('queued','preparing','ready','served') AND oi.status != 'cancelled'
        `).all(t.id) as any[];

        let itemCount = 0;
        let unpaidSubtotal = 0;
        let earliestQueued: string | null = null;
        let maxPrepMin = 0;

        const allOrderItems = db.prepare(`
          SELECT oi.quantity, oi.unit_price, oi.prep_time_min, o.status as order_status, o.created_at
          FROM order_items oi
          JOIN orders o ON oi.order_id = o.id
          WHERE o.table_id = ? AND o.status IN ('queued','preparing','ready','served') AND oi.status != 'cancelled'
        `).all(t.id) as any[];

        allOrderItems.forEach(oi => {
          itemCount += oi.quantity;
          unpaidSubtotal += oi.quantity * oi.unit_price;
          if (['queued', 'preparing'].includes(oi.order_status)) {
            if (oi.prep_time_min > maxPrepMin) maxPrepMin = oi.prep_time_min;
            if (!earliestQueued || new Date(oi.created_at) < new Date(earliestQueued)) {
              earliestQueued = oi.created_at;
            }
          }
        });

        return {
          ...t,
          itemCount,
          unpaidSubtotal,
          earliestQueued,
          estimatedRemainingPrepMinutes: maxPrepMin
        };
      });
      return tablesWithDetails;
    },

    updateTable(id: number, patch: { status?: string; stage?: string; guest_count?: number; reserved?: number }) {
      const fields = [];
      const values: any[] = [];
      for (const [k, v] of Object.entries(patch)) {
        if (v !== undefined) {
          fields.push(`${k} = ?`);
          values.push(v);
        }
      }
      if (fields.length === 0) return db.prepare('SELECT * FROM tables WHERE id = ?').get(id);

      fields.push('updated_at = ?');
      values.push(new Date().toISOString());
      values.push(id);

      db.prepare(`UPDATE tables SET ${fields.join(', ')} WHERE id = ?`).run(...values as any[]);
      const table = db.prepare('SELECT * FROM tables WHERE id = ?').get(id);
      broadcast('table.updated', table);
      return table;
    },

    listMenu(filters?: { station?: string; categoryId?: number }) {
      const categories = db.prepare('SELECT * FROM categories ORDER BY sort_order').all();
      let query = 'SELECT * FROM items WHERE 1=1';
      const params: any[] = [];
      if (filters?.station) {
        query += ' AND station = ?';
        params.push(filters.station);
      }
      if (filters?.categoryId) {
        query += ' AND category_id = ?';
        params.push(filters.categoryId);
      }
      const items = db.prepare(query).all(...params);
      const topItems = db.prepare('SELECT * FROM items ORDER BY order_count DESC LIMIT 10').all();
      return { categories, items, topItems };
    },

    createMenuItem(item: any) {
      const res = db.prepare(`
        INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, station, available, created_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
      `).run(item.name, item.price, item.price_unit || 'VND', item.average_make_time || 10, item.average_make_time_unit || 'min', item.category_id, item.station || 'bar', item.available ?? 1, new Date().toISOString());
      const newItem = db.prepare('SELECT * FROM items WHERE id = ?').get(res.lastInsertRowid);
      broadcast('menu.updated', newItem);
      return newItem;
    },

    updateMenuItem(id: number, patch: any) {
      const fields = [];
      const values: any[] = [];
      for (const [k, v] of Object.entries(patch)) {
        if (v !== undefined) {
          fields.push(`${k} = ?`);
          values.push(v);
        }
      }
      if (fields.length > 0) {
        values.push(id);
        db.prepare(`UPDATE items SET ${fields.join(', ')} WHERE id = ?`).run(...values);
      }
      const item = db.prepare('SELECT * FROM items WHERE id = ?').get(id);
      broadcast('menu.updated', item);
      return item;
    },

    deleteMenuItem(id: number) {
      const used = db.prepare('SELECT id FROM order_items WHERE item_id = ? LIMIT 1').get(id);
      if (used) {
        db.prepare('UPDATE items SET available = 0 WHERE id = ?').run(id);
        const item = db.prepare('SELECT * FROM items WHERE id = ?').get(id);
        broadcast('menu.updated', item);
        return { success: true, archived: true };
      } else {
        db.prepare('DELETE FROM items WHERE id = ?').run(id);
        broadcast('menu.updated', { id, deleted: true });
        return { success: true };
      }
    },

    createOrder(data: { tableId: number; userId: number; source?: string; notes?: string; items: { itemId: number; quantity: number; notes?: string }[] }) {
      const table = db.prepare('SELECT * FROM tables WHERE id = ?').get(data.tableId) as any;
      if (!table) throw new Error('Table not found');
      if (!data.items || data.items.length === 0) throw new Error('Items required');

      let maxTicket = db.prepare('SELECT MAX(ticket_number) as max_ticket FROM orders WHERE date(created_at) = date(?)').get(new Date().toISOString()) as { max_ticket: number };
      const ticketNumber = (maxTicket.max_ticket || 0) + 1;

      db.exec('BEGIN TRANSACTION');
      try {
        const orderRes = db.prepare(`
          INSERT INTO orders (ticket_number, table_id, user_id, source, status, notes, created_at)
          VALUES (?, ?, ?, ?, 'queued', ?, ?)
        `).run(ticketNumber, data.tableId, data.userId, data.source || 'waiter', data.notes || '', new Date().toISOString());
        
        const orderId = orderRes.lastInsertRowid;
        
        let needsKitchen = false;
        let needsBar = false;

        const insertItem = db.prepare(`
          INSERT INTO order_items (order_id, item_id, item_name, quantity, unit_price, prep_time_min, station, notes, status)
          VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'queued')
        `);

        const updateItemCount = db.prepare('UPDATE items SET order_count = order_count + ? WHERE id = ?');

        for (const reqItem of data.items) {
          if (reqItem.quantity <= 0) throw new Error('Quantity must be > 0');
          const item = db.prepare('SELECT * FROM items WHERE id = ?').get(reqItem.itemId) as any;
          if (!item) throw new Error(`Item ${reqItem.itemId} not found`);
          if (item.available !== 1) throw new Error(`Item ${item.name} is unavailable`);

          insertItem.run(orderId, item.id, item.name, reqItem.quantity, item.price, item.average_make_time, item.station, reqItem.notes || '');
          updateItemCount.run(reqItem.quantity, item.id);

          if (item.station === 'kitchen') needsKitchen = true;
          if (item.station === 'bar') needsBar = true;
        }

        // Update table
        const tableStatus = 'occupied';
        let tableStage = table.stage;
        if (table.stage === 'available') {
          // simple logic for stage
          tableStage = needsKitchen ? 'mains' : 'starter'; // simplified
        }
        db.prepare('UPDATE tables SET status = ?, stage = ?, updated_at = ? WHERE id = ?').run(tableStatus, tableStage, new Date().toISOString(), table.id);

        db.exec('COMMIT');

        const newOrder = db.prepare('SELECT * FROM orders WHERE id = ?').get(orderId);
        broadcast('order.created', newOrder);
        broadcast('table.updated', db.prepare('SELECT * FROM tables WHERE id = ?').get(table.id));

        return newOrder;
      } catch (err) {
        db.exec('ROLLBACK');
        throw err;
      }
    },

    listOrders(filters?: { tableId?: number }) {
      let query = `
        SELECT o.*, t.name as table_name 
        FROM orders o 
        JOIN tables t ON o.table_id = t.id 
        WHERE o.status IN ('queued', 'preparing', 'ready', 'served')
      `;
      const params = [];
      if (filters?.tableId) {
        query += ` AND o.table_id = ?`;
        params.push(filters.tableId);
      }
      query += ` ORDER BY o.created_at DESC`;
      
      const orders = db.prepare(query).all(...params) as any[];
      
      return orders.map(order => {
        const items = db.prepare('SELECT * FROM order_items WHERE order_id = ?').all(order.id) as any[];
        
        let estimatedPrepMin = 0;
        items.forEach(i => {
          if (i.status !== 'cancelled' && i.prep_time_min > estimatedPrepMin) {
            estimatedPrepMin = i.prep_time_min;
          }
        });

        const elapsedSec = Math.floor((Date.now() - new Date(order.created_at).getTime()) / 1000);
        const estimatedPrepSec = estimatedPrepMin * 60;
        const remainingSec = estimatedPrepSec - elapsedSec;
        
        let slaStatus = 'on_time';
        if (remainingSec < 0) slaStatus = 'overdue';
        else if (remainingSec < 120) slaStatus = 'due_soon'; // < 2 mins

        return {
          ...order,
          items,
          estimatedPrepMin,
          elapsedSec,
          remainingSec,
          slaStatus
        };
      });
    },

    listKitchenQueue(station?: string) {
      let query = `
        SELECT o.*, t.name as table_name 
        FROM orders o 
        JOIN tables t ON o.table_id = t.id 
        WHERE o.status IN ('queued', 'preparing', 'ready')
      `;
      const params: any[] = [];
      // If station filtering, we only include orders that have items for that station.
      if (station) {
        query += ` AND EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.station = ?)`;
        params.push(station);
      }
      query += ` ORDER BY o.created_at ASC, o.id ASC`;
      
      const orders = db.prepare(query).all(...params) as any[];
      let fifoPosition = 1;
      
      return orders.map(order => {
        let itemsQuery = 'SELECT * FROM order_items WHERE order_id = ?';
        const itemsParams: any[] = [order.id];
        if (station) {
          itemsQuery += ` AND station = ?`;
          itemsParams.push(station);
        }
        const items = db.prepare(itemsQuery).all(...itemsParams) as any[];
        
        let estimatedPrepMin = 0;
        items.forEach(i => {
          if (i.status !== 'cancelled' && i.prep_time_min > estimatedPrepMin) {
            estimatedPrepMin = i.prep_time_min;
          }
        });

        const elapsedSec = Math.floor((Date.now() - new Date(order.created_at).getTime()) / 1000);
        const estimatedPrepSec = estimatedPrepMin * 60;
        const remainingSec = estimatedPrepSec - elapsedSec;
        
        let slaStatus = 'on_time';
        if (remainingSec < 0) slaStatus = 'overdue';
        else if (remainingSec < 120) slaStatus = 'due_soon'; // < 2 mins

        let currentPos = null;
        if (order.status === 'queued' || order.status === 'preparing') {
          currentPos = fifoPosition++;
        }

        return {
          ...order,
          items,
          fifoPosition: currentPos,
          estimatedPrepMin,
          elapsedSec,
          remainingSec,
          slaStatus
        };
      });
    },

    advanceOrderStatus(orderId: number, targetStatus: string, opts?: { forceFifoOverride?: boolean }) {
      const order = db.prepare('SELECT * FROM orders WHERE id = ?').get(orderId) as any;
      if (!order) throw new Error('Order not found');

      // FIFO check
      if (order.status === 'queued' && targetStatus === 'preparing') {
        const earlierQueued = db.prepare(`
          SELECT o.id, o.ticket_number, o.created_at, t.name as table_name
          FROM orders o
          JOIN tables t ON o.table_id = t.id
          WHERE o.status = 'queued' AND (o.created_at < ? OR (o.created_at = ? AND o.id < ?))
        `).all(order.created_at, order.created_at, order.id);

        if (earlierQueued.length > 0 && !opts?.forceFifoOverride) {
          throw { fifoViolation: true, earlierOrders: earlierQueued };
        }
      }

      const updates = ['status = ?'];
      const values: any[] = [targetStatus];

      const now = new Date().toISOString();
      if (targetStatus === 'preparing' && !order.started_at) {
        updates.push('started_at = ?'); values.push(now);
      } else if (targetStatus === 'ready' && !order.ready_at) {
        updates.push('ready_at = ?'); values.push(now);
      } else if (targetStatus === 'served' && !order.served_at) {
        updates.push('served_at = ?'); values.push(now);
      } else if (targetStatus === 'paid' && !order.paid_at) {
        updates.push('paid_at = ?'); values.push(now);
      }

      values.push(orderId);

      db.exec('BEGIN TRANSACTION');
      try {
        db.prepare(`UPDATE orders SET ${updates.join(', ')} WHERE id = ?`).run(...values);
        
        if (targetStatus === 'preparing') {
          db.prepare("UPDATE order_items SET status = 'preparing' WHERE order_id = ? AND status = 'queued'").run(orderId);
        } else if (targetStatus === 'ready') {
          db.prepare("UPDATE order_items SET status = 'ready' WHERE order_id = ? AND status IN ('queued', 'preparing')").run(orderId);
        } else if (targetStatus === 'served') {
          db.prepare("UPDATE order_items SET status = 'served' WHERE order_id = ? AND status != 'cancelled'").run(orderId);
        } else if (targetStatus === 'cancelled') {
          db.prepare("UPDATE order_items SET status = 'cancelled' WHERE order_id = ?").run(orderId);
        }

        db.exec('COMMIT');

        const updatedOrder = db.prepare('SELECT * FROM orders WHERE id = ?').get(orderId);
        broadcast('order.updated', updatedOrder);
        broadcast('table.updated', db.prepare('SELECT * FROM tables WHERE id = ?').get(order.table_id));

        return updatedOrder;
      } catch (e) {
        db.exec('ROLLBACK');
        throw e;
      }
    },

    advanceOrderItemStatus(orderItemId: number, targetStatus: string) {
      if (!['queued', 'preparing', 'ready', 'served', 'cancelled'].includes(targetStatus)) {
        throw new Error('Invalid targetStatus');
      }

      const item = db.prepare('SELECT * FROM order_items WHERE id = ?').get(orderItemId) as any;
      if (!item) throw new Error('Order item not found');

      db.exec('BEGIN TRANSACTION');
      try {
        db.prepare('UPDATE order_items SET status = ? WHERE id = ?').run(targetStatus, orderItemId);
        
        const allItems = db.prepare('SELECT status FROM order_items WHERE order_id = ? AND status != ?').all(item.order_id, 'cancelled') as any[];
        
        const order = db.prepare('SELECT * FROM orders WHERE id = ?').get(item.order_id) as any;
        const now = new Date().toISOString();

        if (allItems.length > 0 && allItems.every(i => i.status === 'served')) {
          db.prepare('UPDATE orders SET status = ?, served_at = COALESCE(served_at, ?) WHERE id = ?').run('served', now, order.id);
        } else if (allItems.length > 0 && allItems.every(i => i.status === 'ready' || i.status === 'served')) {
          db.prepare('UPDATE orders SET status = ?, started_at = COALESCE(started_at, ?), ready_at = COALESCE(ready_at, ?) WHERE id = ?').run('ready', now, now, order.id);
        } else if (allItems.some(i => ['preparing', 'ready', 'served'].includes(i.status))) {
          db.prepare('UPDATE orders SET status = ?, started_at = COALESCE(started_at, ?) WHERE id = ?').run('preparing', now, order.id);
        } else {
          db.prepare('UPDATE orders SET status = ? WHERE id = ?').run('queued', order.id);
        }

        db.exec('COMMIT');

        const updatedOrder = db.prepare('SELECT * FROM orders WHERE id = ?').get(item.order_id);
        const updatedItem = db.prepare('SELECT * FROM order_items WHERE id = ?').get(orderItemId);

        broadcast('order.updated', updatedOrder);
        broadcast('table.updated', db.prepare('SELECT * FROM tables WHERE id = ?').get(order.table_id));

        return { item: updatedItem, order: updatedOrder };
      } catch (e) {
        db.exec('ROLLBACK');
        throw e;
      }
    },

    getTableBill(tableId: number) {
      const orders = db.prepare(`
        SELECT * FROM orders 
        WHERE table_id = ? AND status IN ('queued','preparing','ready','served')
      `).all(tableId) as any[];

      const lineItems = [];
      let subtotal = 0;
      let taxRate = 10; // default
      let discountRate = 0;

      for (const o of orders) {
        if (o.tax_rate !== null && o.tax_rate !== undefined) taxRate = o.tax_rate;
        if (o.discount_rate !== null && o.discount_rate !== undefined) discountRate = o.discount_rate;

        const items = db.prepare('SELECT * FROM order_items WHERE order_id = ? AND status != ?').all(o.id, 'cancelled') as any[];
        for (const i of items) {
          let remainingMin = 0;
          if (['queued', 'preparing'].includes(i.status)) {
             const elapsedMin = (Date.now() - new Date(o.created_at).getTime()) / 60000;
             remainingMin = Math.max(0, Math.ceil(i.prep_time_min - elapsedMin));
          }

          lineItems.push({
            id: i.id,
            orderId: i.order_id,
            name: i.item_name,
            quantity: i.quantity,
            unit_price: i.unit_price,
            total: i.quantity * i.unit_price,
            status: i.status,
            prep_time_min: i.prep_time_min,
            notes: i.notes,
            remainingMin
          });
          subtotal += i.quantity * i.unit_price;
        }
      }

      const discountAmount = subtotal * (discountRate / 100);
      const subtotalAfterDiscount = subtotal - discountAmount;
      const taxAmount = subtotalAfterDiscount * (taxRate / 100);
      const grandTotal = subtotalAfterDiscount + taxAmount;

      return {
        tableId,
        lineItems,
        subtotal,
        taxRate,
        discountRate,
        discountAmount,
        taxAmount,
        grandTotal
      };
    },

    updateTableBillRates(tableId: number, rates: { taxRate?: number; discountRate?: number }) {
      const fields = [];
      const values: any[] = [];
      if (rates.taxRate !== undefined) { fields.push('tax_rate = ?'); values.push(rates.taxRate); }
      if (rates.discountRate !== undefined) { fields.push('discount_rate = ?'); values.push(rates.discountRate); }

      if (fields.length > 0) {
        values.push(tableId);
        db.prepare(`UPDATE orders SET ${fields.join(', ')} WHERE table_id = ? AND status IN ('queued','preparing','ready','served')`).run(...values);
      }
      return this.getTableBill(tableId);
    },

    payTableBill(tableId: number, rates?: { taxRate?: number; discountRate?: number }) {
      if (rates) this.updateTableBillRates(tableId, rates);
      
      const bill = this.getTableBill(tableId);
      if (bill.lineItems.length === 0) {
        throw new Error('No active unpaid items for this table');
      }

      db.exec('BEGIN TRANSACTION');
      try {
        const now = new Date().toISOString();
        db.prepare(`
          UPDATE orders 
          SET status = 'paid', paid_at = ? 
          WHERE table_id = ? AND status IN ('queued','preparing','ready','served')
        `).run(now, tableId);

        db.prepare(`
          UPDATE tables 
          SET status = 'available', stage = 'available', guest_count = 0, reserved = 0, updated_at = ? 
          WHERE id = ?
        `).run(now, tableId);

        db.exec('COMMIT');

        broadcast('order.paid', { tableId });
        broadcast('table.updated', db.prepare('SELECT * FROM tables WHERE id = ?').get(tableId));

        return { success: true, billPaid: bill };
      } catch (e) {
        db.exec('ROLLBACK');
        throw e;
      }
    },

    getAnalytics() {
      const today = new Date().toISOString().split('T')[0];
      
      const revenueRow = db.prepare(`
        SELECT SUM(oi.quantity * oi.unit_price) as total_revenue
        FROM order_items oi
        JOIN orders o ON oi.order_id = o.id
        WHERE o.status = 'paid' AND date(o.paid_at) = ? AND oi.status != 'cancelled'
      `).get(today) as any;

      const activeOrdersRow = db.prepare(`
        SELECT COUNT(*) as active_orders
        FROM orders
        WHERE status IN ('queued','preparing','ready','served')
      `).get() as any;

      const paidOrdersRow = db.prepare(`
        SELECT COUNT(*) as paid_orders_count
        FROM orders
        WHERE status = 'paid' AND date(paid_at) = ?
      `).get(today) as any;

      const stationBreakdownRow = db.prepare(`
        SELECT station, SUM(quantity) as active_count
        FROM order_items oi
        JOIN orders o ON oi.order_id = o.id
        WHERE o.status IN ('queued','preparing','ready','served') AND oi.status IN ('queued','preparing')
        GROUP BY station
      `).all() as any[];

      const stationBreakdown = { kitchen: 0, bar: 0 } as any;
      for (const row of stationBreakdownRow) {
        if (row.station === 'kitchen') stationBreakdown.kitchen = row.active_count;
        if (row.station === 'bar') stationBreakdown.bar = row.active_count;
      }

      // avg prep time in minutes
      const completedOrders = db.prepare(`
        SELECT started_at, ready_at FROM orders 
        WHERE started_at IS NOT NULL AND ready_at IS NOT NULL
      `).all() as any[];

      let totalPrepSec = 0;
      completedOrders.forEach(o => {
        totalPrepSec += (new Date(o.ready_at).getTime() - new Date(o.started_at).getTime()) / 1000;
      });
      const avgPrepTimeMin = completedOrders.length > 0 ? (totalPrepSec / completedOrders.length) / 60 : 0;

      const topItems = db.prepare('SELECT name, order_count FROM items ORDER BY order_count DESC LIMIT 10').all();

      return {
        revenueToday: revenueRow?.total_revenue || 0,
        activeOrders: activeOrdersRow?.active_orders || 0,
        paidOrdersCount: paidOrdersRow?.paid_orders_count || 0,
        stationBreakdown,
        avgPrepTimeMin,
        topItems
      };
    }
  };
}
