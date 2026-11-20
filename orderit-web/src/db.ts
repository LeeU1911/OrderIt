import { DatabaseSync } from 'node:sqlite';
import { mkdirSync } from 'node:fs';
import { dirname } from 'node:path';

export function getDb(dbPath: string = './data/orderit.db'): DatabaseSync {
  if (dbPath !== ':memory:') {
    mkdirSync(dirname(dbPath), { recursive: true });
  }

  const db = new DatabaseSync(dbPath);
  db.exec('PRAGMA foreign_keys = ON;');
  db.exec('PRAGMA journal_mode = WAL;');

  // Create tables
  db.exec(`
    CREATE TABLE IF NOT EXISTS businesses (
      id INTEGER PRIMARY KEY,
      name TEXT,
      address TEXT,
      currency TEXT DEFAULT 'VND',
      default_tax_rate REAL DEFAULT 10
    );

    CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY,
      username TEXT UNIQUE,
      password TEXT,
      display_name TEXT,
      role TEXT CHECK(role IN ('waiter','kitchen','manager')),
      business_id INTEGER
    );

    CREATE TABLE IF NOT EXISTS tables (
      id INTEGER PRIMARY KEY,
      business_id INTEGER,
      name TEXT NOT NULL,
      status TEXT CHECK(status IN ('available','occupied','bill_requested')) DEFAULT 'available',
      stage TEXT CHECK(stage IN ('available','starter','mains','dessert','bill')) DEFAULT 'available',
      guest_count INTEGER DEFAULT 0,
      reserved INTEGER DEFAULT 0,
      updated_at TEXT
    );

    CREATE TABLE IF NOT EXISTS categories (
      id INTEGER PRIMARY KEY,
      name TEXT NOT NULL,
      station TEXT CHECK(station IN ('kitchen','bar')) DEFAULT 'bar',
      icon TEXT,
      sort_order INTEGER DEFAULT 0
    );

    CREATE TABLE IF NOT EXISTS items (
      id INTEGER PRIMARY KEY,
      name TEXT NOT NULL,
      price INTEGER NOT NULL,
      price_unit TEXT DEFAULT 'VND',
      average_make_time INTEGER NOT NULL DEFAULT 10,
      average_make_time_unit TEXT DEFAULT 'min',
      category_id INTEGER NOT NULL REFERENCES categories(id),
      station TEXT CHECK(station IN ('kitchen','bar')) DEFAULT 'bar',
      available INTEGER DEFAULT 1,
      order_count INTEGER DEFAULT 0,
      created_at TEXT
    );

    CREATE TABLE IF NOT EXISTS orders (
      id INTEGER PRIMARY KEY,
      ticket_number INTEGER NOT NULL,
      table_id INTEGER NOT NULL REFERENCES tables(id),
      user_id INTEGER REFERENCES users(id),
      source TEXT CHECK(source IN ('waiter','customer')) DEFAULT 'waiter',
      status TEXT CHECK(status IN ('queued','preparing','ready','served','paid','cancelled')) DEFAULT 'queued',
      notes TEXT DEFAULT '',
      tax_rate REAL DEFAULT 10,
      discount_rate REAL DEFAULT 0,
      created_at TEXT NOT NULL,
      started_at TEXT,
      ready_at TEXT,
      served_at TEXT,
      paid_at TEXT
    );

    CREATE TABLE IF NOT EXISTS order_items (
      id INTEGER PRIMARY KEY,
      order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
      item_id INTEGER NOT NULL REFERENCES items(id),
      item_name TEXT NOT NULL,
      quantity INTEGER NOT NULL,
      unit_price INTEGER NOT NULL,
      prep_time_min INTEGER NOT NULL,
      station TEXT NOT NULL,
      notes TEXT DEFAULT '',
      status TEXT CHECK(status IN ('queued','preparing','ready','served','cancelled')) DEFAULT 'queued'
    );
  `);

  return db;
}
