import express from 'express';
import { getDb } from './db.js';
import { seedDb } from './seed.js';
import { createServices } from './services.js';
import { addClient } from './events.js';

export function createApp(dbPath?: string) {
  const db = getDb(dbPath);
  seedDb(db);
  const services = createServices(db);

  const app = express();
  app.use(express.json());
  app.use(express.static('public'));

  app.get('/api/health', (req, res) => {
    res.json({ status: 'ok' });
  });

  app.get('/api/events', (req, res) => {
    addClient(res);
  });

  app.post('/api/auth/login', (req, res) => {
    try {
      const { username, password } = req.body;
      const result = services.login(username, password);
      res.json(result);
    } catch (e: any) {
      res.status(401).json({ error: e.message });
    }
  });

  app.get('/api/tables', (req, res) => {
    res.json(services.listTables());
  });

  app.post('/api/tables', (req, res) => {
    try {
      res.json(services.createTable(req.body.name));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.delete('/api/tables/:id', (req, res) => {
    try {
      res.json(services.deleteTable(parseInt(req.params.id)));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.patch('/api/tables/:id', (req, res) => {
    try {
      res.json(services.updateTable(parseInt(req.params.id), req.body));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.get('/api/menu', (req, res) => {
    const filters: any = {};
    if (req.query.station) filters.station = req.query.station;
    if (req.query.categoryId) filters.categoryId = parseInt(req.query.categoryId as string);
    res.json(services.listMenu(filters));
  });

  app.post('/api/menu/items', (req, res) => {
    try {
      res.json(services.createMenuItem(req.body));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.patch('/api/menu/items/:id', (req, res) => {
    try {
      res.json(services.updateMenuItem(parseInt(req.params.id), req.body));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.delete('/api/menu/items/:id', (req, res) => {
    try {
      res.json(services.deleteMenuItem(parseInt(req.params.id)));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.post('/api/orders', (req, res) => {
    try {
      res.json(services.createOrder(req.body));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.get('/api/orders', (req, res) => {
    try {
      const filters: any = {};
      if (req.query.tableId) filters.tableId = parseInt(req.query.tableId as string);
      res.json(services.listOrders(filters));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.get('/api/orders/kitchen', (req, res) => {
    try {
      res.json(services.listKitchenQueue(req.query.station as string));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.patch('/api/orders/:id/status', (req, res) => {
    try {
      res.json(services.advanceOrderStatus(parseInt(req.params.id), req.body.status, req.body.opts));
    } catch (e: any) {
      if (e.fifoViolation) {
        res.status(409).json(e);
      } else {
        res.status(400).json({ error: e.message });
      }
    }
  });

  app.patch('/api/order-items/:id/status', (req, res) => {
    try {
      res.json(services.advanceOrderItemStatus(parseInt(req.params.id), req.body.status));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.get('/api/tables/:id/bill', (req, res) => {
    try {
      res.json(services.getTableBill(parseInt(req.params.id)));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.patch('/api/tables/:id/bill', (req, res) => {
    try {
      res.json(services.updateTableBillRates(parseInt(req.params.id), req.body));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.post('/api/tables/:id/pay', (req, res) => {
    try {
      res.json(services.payTableBill(parseInt(req.params.id), req.body));
    } catch (e: any) {
      res.status(400).json({ error: e.message });
    }
  });

  app.get('/api/analytics', (req, res) => {
    try {
      res.json(services.getAnalytics());
    } catch (e: any) {
      res.status(500).json({ error: e.message });
    }
  });

  return { app, db, close: () => db.close() };
}
