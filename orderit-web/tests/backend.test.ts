import { describe, it, before, after } from 'node:test';
import assert from 'node:assert/strict';
import { createApp } from '../src/app.js';
import { Server } from 'node:http';

describe('OrderIt Backend', () => {
  let app: any;
  let server: Server;
  let baseUrl: string;
  let closeDb: () => void;

  before(async () => {
    const created = createApp(':memory:');
    app = created.app;
    closeDb = created.close;
    
    await new Promise<void>((resolve) => {
      server = app.listen(0, () => {
        const address = server.address() as any;
        baseUrl = `http://localhost:${address.port}`;
        resolve();
      });
    });
  });

  after(() => {
    server.close();
    closeDb();
  });

  it('should return health status', async () => {
    const res = await fetch(`${baseUrl}/api/health`);
    const data = await res.json();
    assert.equal(res.status, 200);
    assert.equal(data.status, 'ok');
  });

  it('should login successfully', async () => {
    const res = await fetch(`${baseUrl}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: 'staff1', password: 'password' })
    });
    const data = await res.json();
    assert.equal(res.status, 200);
    assert.equal(data.user.username, 'staff1');
    assert.ok(data.token);
  });

  it('should list tables with their summary', async () => {
    const res = await fetch(`${baseUrl}/api/tables`);
    const tables = await res.json();
    assert.equal(res.status, 200);
    assert.equal(tables.length, 10);
    
    // Table 1 is seeded with occupied/starter
    const table1 = tables.find((t: any) => t.name === 'Table 1');
    assert.equal(table1.status, 'occupied');
    assert.equal(table1.stage, 'starter');
    assert.ok(table1.itemCount > 0);
  });

  it('should list menu items', async () => {
    const res = await fetch(`${baseUrl}/api/menu`);
    const menu = await res.json();
    assert.equal(res.status, 200);
    assert.ok(menu.categories.length > 0);
    assert.ok(menu.items.length > 0);
  });

  it('should reject orders containing an unavailable menu item', async () => {
    const resMenu = await fetch(`${baseUrl}/api/menu`);
    const menu = await resMenu.json();
    const item = menu.items[0];

    // Mark item as unavailable
    await fetch(`${baseUrl}/api/menu/items/${item.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ available: 0 })
    });

    const res = await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        tableId: 3,
        userId: 1,
        items: [{ itemId: item.id, quantity: 1 }]
      })
    });
    const data = await res.json();
    assert.equal(res.status, 400);
    assert.ok(data.error.includes('unavailable'));

    // Revert it
    await fetch(`${baseUrl}/api/menu/items/${item.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ available: 1 })
    });
  });

  it('should create and delete tables, rejecting delete if active orders exist', async () => {
    const resPost = await fetch(`${baseUrl}/api/tables`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: 'Test Table' })
    });
    const newTable = await resPost.json();
    if (resPost.status !== 200) console.error("POST TABLE ERR:", newTable);
    assert.equal(resPost.status, 200);
    assert.equal(newTable.name, 'Test Table');

    const resDelete = await fetch(`${baseUrl}/api/tables/${newTable.id}`, {
      method: 'DELETE'
    });
    const dataDel = await resDelete.json();
    if (resDelete.status !== 200) console.error("DELETE FAIL:", dataDel);
    assert.equal(resDelete.status, 200);

    // Reject deletion with active order (Table 1 is occupied)
    const resDeleteActive = await fetch(`${baseUrl}/api/tables/1`, {
      method: 'DELETE'
    });
    assert.equal(resDeleteActive.status, 400);
    const dataActive = await resDeleteActive.json();
    assert.ok(dataActive.error.includes('active orders'));
  });

  let newOrderId: number;

  it('should create an order', async () => {
    // get some items
    const menuRes = await fetch(`${baseUrl}/api/menu`);
    const menu = await menuRes.json();
    const item = menu.items[0];

    const res = await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        tableId: 3,
        userId: 1,
        items: [{ itemId: item.id, quantity: 2 }]
      })
    });
    const order = await res.json();
    assert.equal(res.status, 200);
    assert.equal(order.status, 'queued');
    newOrderId = order.id;

    // Verify table updated
    const tableRes = await fetch(`${baseUrl}/api/tables`);
    const tables = await tableRes.json();
    const table3 = tables.find((t: any) => t.id === 3);
    assert.equal(table3.status, 'occupied');
  });

  it('should enforce FIFO on kitchen queue', async () => {
    // Create another order for table 4
    const menuRes = await fetch(`${baseUrl}/api/menu`);
    const menu = await menuRes.json();
    const item = menu.items[1];

    const res2 = await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        tableId: 4,
        userId: 1,
        items: [{ itemId: item.id, quantity: 1 }]
      })
    });
    const order2 = await res2.json();

    // Try to advance order2 to 'preparing' without force
    const advanceRes = await fetch(`${baseUrl}/api/orders/${order2.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'preparing' })
    });
    assert.equal(advanceRes.status, 409); // Conflict - FIFO violation
    const advanceData = await advanceRes.json();
    assert.equal(advanceData.fifoViolation, true);

    // Try with forceFifoOverride
    const advanceForceRes = await fetch(`${baseUrl}/api/orders/${order2.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'preparing', opts: { forceFifoOverride: true } })
    });
    const txt = await advanceForceRes.text(); if(advanceForceRes.status !== 200) console.error(txt); assert.equal(advanceForceRes.status, 200);
  });

  it('should list orders by tableId with timers and items', async () => {
    const res = await fetch(`${baseUrl}/api/orders?tableId=3`);
    const orders = await res.json();
    assert.equal(res.status, 200);
    assert.ok(orders.length > 0);
    const order = orders[0];
    assert.equal(order.table_id, 3);
    assert.ok(Array.isArray(order.items));
    assert.ok('estimatedPrepMin' in order);
    assert.ok('remainingSec' in order);
    assert.ok('slaStatus' in order);
  });

  it('should auto-advance parent order to ready when all items are ready', async () => {
    const menuRes = await fetch(`${baseUrl}/api/menu`);
    const menu = await menuRes.json();
    const item = menu.items[2];

    const orderRes = await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        tableId: 5,
        userId: 1,
        items: [{ itemId: item.id, quantity: 1 }]
      })
    });
    const order = await orderRes.json();
    
    // Fetch items for this order
    const listRes = await fetch(`${baseUrl}/api/orders?tableId=5`);
    const orders = await listRes.json();
    const targetOrder = orders.find((o: any) => o.id === order.id);
    const orderItem = targetOrder.items[0];

    const patchRes = await fetch(`${baseUrl}/api/order-items/${orderItem.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'ready' })
    });
    const updatedResponse = await patchRes.json();
    assert.equal(patchRes.status, 200);
    assert.equal(updatedResponse.item.status, 'ready');

    // Check parent order
    const listRes2 = await fetch(`${baseUrl}/api/orders?tableId=5`);
    const orders2 = await listRes2.json();
    const targetOrder2 = orders2.find((o: any) => o.id === order.id);
    assert.equal(targetOrder2.status, 'ready');
  });

  it('should get bill and pay it', async () => {
    const billRes = await fetch(`${baseUrl}/api/tables/3/bill`);
    const bill = await billRes.json();
    assert.equal(billRes.status, 200);
    assert.ok(bill.grandTotal > 0);

    const payRes = await fetch(`${baseUrl}/api/tables/3/pay`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({})
    });
    const payData = await payRes.json();
    assert.equal(payRes.status, 200);
    assert.equal(payData.success, true);

    // Verify table is available again
    const tableRes = await fetch(`${baseUrl}/api/tables`);
    const tables = await tableRes.json();
    const table3 = tables.find((t: any) => t.id === 3);
    assert.equal(table3.status, 'available');
    assert.equal(table3.guest_count, 0);
  });

  it('should get analytics', async () => {
    const res = await fetch(`${baseUrl}/api/analytics`);
    const data = await res.json();
    assert.equal(res.status, 200);
    assert.ok(data.revenueToday >= 0);
  });

  it('should partially advance a multi-item order and broadcast via SSE', async () => {
    // 1. Create a multi-item order
    const menuRes = await fetch(`${baseUrl}/api/menu`);
    const menu = await menuRes.json();
    const item1 = menu.items[0];
    const item2 = menu.items[1];

    const orderRes = await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        tableId: 7,
        userId: 1,
        items: [
          { itemId: item1.id, quantity: 1 },
          { itemId: item2.id, quantity: 1 }
        ]
      })
    });
    const order = await orderRes.json();

    // Fetch items
    const listRes = await fetch(`${baseUrl}/api/orders?tableId=7`);
    const orders = await listRes.json();
    const targetOrder = orders.find((o: any) => o.id === order.id);
    const orderItem1 = targetOrder.items[0];
    const orderItem2 = targetOrder.items[1];

    assert.equal(targetOrder.status, 'queued');
    assert.equal(orderItem1.status, 'queued');
    assert.equal(orderItem2.status, 'queued');

    // Set up SSE listener for order.updated
    const controller = new AbortController();
    const ssePromise = fetch(`${baseUrl}/api/events`, { signal: controller.signal });
    await new Promise(r => setTimeout(r, 100));

    // Change one item to ready
    const patchRes = await fetch(`${baseUrl}/api/order-items/${orderItem1.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'ready' })
    });
    const updatedResponse = await patchRes.json();
    assert.equal(patchRes.status, 200);
    assert.equal(updatedResponse.item.status, 'ready');
    assert.equal(updatedResponse.order.status, 'preparing'); // Parent is now preparing

    // Read SSE stream
    const sseRes = await ssePromise;
    const reader = sseRes.body?.getReader();
    assert.ok(reader);
    
    const decoder = new TextDecoder();
    let eventsText = '';
    
    // We only want to read a few events and then abort to not hang
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      eventsText += decoder.decode(value, { stream: true });
      if (eventsText.includes('"type":"order.updated"')) {
        break;
      }
    }
    controller.abort();

    assert.ok(eventsText.includes('"type":"order.updated"'));

    // Check parent order again
    const listRes2 = await fetch(`${baseUrl}/api/orders?tableId=7`);
    const orders2 = await listRes2.json();
    const targetOrder2 = orders2.find((o: any) => o.id === order.id);
    assert.equal(targetOrder2.status, 'preparing'); // Parent moved to preparing
    
    const fetchedItem1 = targetOrder2.items.find((i: any) => i.id === orderItem1.id);
    const fetchedItem2 = targetOrder2.items.find((i: any) => i.id === orderItem2.id);
    assert.equal(fetchedItem1.status, 'ready');
    assert.equal(fetchedItem2.status, 'queued'); // Preserved as queued
  });

  it('should revert parent order to preparing if item unchecked from ready', async () => {
    const listRes = await fetch(`${baseUrl}/api/orders?tableId=5`);
    const orders = await listRes.json();
    const targetOrder = orders[0]; // Used from earlier test where it was set to ready
    const orderItem = targetOrder.items[0];

    assert.equal(targetOrder.status, 'ready');

    const patchRes = await fetch(`${baseUrl}/api/order-items/${orderItem.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'preparing' })
    });
    const updatedResponse = await patchRes.json();
    assert.equal(patchRes.status, 200);
    assert.equal(updatedResponse.item.status, 'preparing');
    assert.equal(updatedResponse.order.status, 'preparing'); // Parent is now preparing
  });

  it('should not wipe a ready item when advancing parent order to preparing', async () => {
    const listRes = await fetch(`${baseUrl}/api/orders?tableId=7`); // using table 7 multi-item order
    const orders = await listRes.json();
    const targetOrder = orders[0];
    
    assert.equal(targetOrder.status, 'preparing'); // It is preparing from previous test
    
    // Explicitly advance parent order to preparing (maybe Chef hits "Start All")
    const patchRes = await fetch(`${baseUrl}/api/orders/${targetOrder.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'preparing' })
    });
    const updatedOrder = await patchRes.json();
    assert.equal(patchRes.status, 200);
    
    // Fetch items again
    const listRes2 = await fetch(`${baseUrl}/api/orders?tableId=7`);
    const orders2 = await listRes2.json();
    const fetchedOrder = orders2.find((o: any) => o.id === targetOrder.id);
    
    // Item 1 was 'ready', it should still be 'ready'. Item 2 was 'queued', it should be 'preparing'.
    const item1 = fetchedOrder.items[0]; // Originally the ready one
    const item2 = fetchedOrder.items[1];
    
    assert.equal(item1.status, 'ready');
    assert.equal(item2.status, 'preparing');
  });

  it('should correctly support 0% tax rate on a table bill', async () => {
    // create a new order on a new table to ensure bill is there
    const menuRes = await fetch(`${baseUrl}/api/menu`);
    const menu = await menuRes.json();
    const item = menu.items[0];

    await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ tableId: 8, userId: 1, items: [{ itemId: item.id, quantity: 1 }] })
    });

    const patchRes = await fetch(`${baseUrl}/api/tables/8/bill`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ taxRate: 0 })
    });
    const updatedBill = await patchRes.json();
    
    assert.equal(patchRes.status, 200);
    assert.equal(updatedBill.taxRate, 0);
    assert.equal(updatedBill.taxAmount, 0);
  });

  it('should accept SSE connection and receive order.created event', async () => {
    const controller = new AbortController();
    const resPromise = fetch(`${baseUrl}/api/events`, { signal: controller.signal });
    
    // Wait a brief moment for the connection to establish
    await new Promise(r => setTimeout(r, 100));

    // trigger order creation
    const menuRes = await fetch(`${baseUrl}/api/menu`);
    const menu = await menuRes.json();
    const item = menu.items[0];

    const orderResPromise = fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        tableId: 6,
        userId: 1,
        items: [{ itemId: item.id, quantity: 1 }]
      })
    });

    const res = await resPromise;
    assert.equal(res.status, 200);
    assert.equal(res.headers.get('content-type'), 'text/event-stream');

    // read SSE stream
    const reader = res.body?.getReader();
    assert.ok(reader);
    
    const decoder = new TextDecoder();
    let eventsText = '';
    
    const readStream = async () => {
      while (true) {
        const { done, value } = await reader.read();
        if (done) break;
        eventsText += decoder.decode(value, { stream: true });
        if (eventsText.includes('"type":"order.created"')) break;
      }
    };
    
    await Promise.all([readStream(), orderResPromise]);

    assert.ok(eventsText.includes('"type":"order.created"'));
    assert.ok(eventsText.includes('data: {"'));
    
    controller.abort();
  });
});
