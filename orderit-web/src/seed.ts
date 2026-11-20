import { DatabaseSync } from 'node:sqlite';

export function seedDb(db: DatabaseSync) {
  const isSeeded = db.prepare(`SELECT count(*) as count FROM businesses`).get() as { count: number };
  if (isSeeded.count > 0) {
    return; // Already seeded
  }

  // Insert Business
  const insertBusiness = db.prepare(`INSERT INTO businesses (name, address, currency, default_tax_rate) VALUES (?, ?, ?, ?)`);
  insertBusiness.run('OrderIt Bistro', 'Cong Hoa', 'VND', 10);
  const businessId = 1;

  // Insert Users
  const insertUser = db.prepare(`INSERT INTO users (username, password, display_name, role, business_id) VALUES (?, ?, ?, ?, ?)`);
  insertUser.run('staff1', 'password', 'Lan Anh (Waiter)', 'waiter', businessId);
  insertUser.run('kitchen1', 'password', 'Chef Minh (Kitchen)', 'kitchen', businessId);
  insertUser.run('manager1', 'password', 'Huy (Manager)', 'manager', businessId);

  // Insert Tables
  const insertTable = db.prepare(`INSERT INTO tables (business_id, name, status, stage, guest_count) VALUES (?, ?, ?, ?, ?)`);
  for (let i = 1; i <= 10; i++) {
    let status = 'available';
    let stage = 'available';
    let guestCount = 0;

    if (i === 1) {
      status = 'occupied';
      stage = 'starter';
      guestCount = 2;
    } else if (i === 2) {
      status = 'bill_requested';
      stage = 'bill';
      guestCount = 4;
    } else if (i === 5) {
      status = 'occupied';
      stage = 'mains';
      guestCount = 3;
    }

    insertTable.run(businessId, `Table ${i}`, status, stage, guestCount);
  }

  // Insert Categories
  const insertCategory = db.prepare(`INSERT INTO categories (name, station, icon, sort_order) VALUES (?, ?, ?, ?)`);
  const categories = [
    { name: 'Starter', station: 'kitchen', icon: 'Menu - Starter.jpg' },
    { name: 'Mains', station: 'kitchen', icon: 'Menu - Food.jpg' },
    { name: 'Ăn sáng', station: 'kitchen', icon: 'Menu - Food.jpg' },
    { name: 'Dessert', station: 'kitchen', icon: 'Menu - Dessert.jpg' },
    { name: 'Café', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Trà', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Nước Trái Cây', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Sinh Tố', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Nước Ngọt', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Mocktail', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Cocktail', station: 'bar', icon: 'Menu - Drink.jpg' },
    { name: 'Bia & Đồ uống', station: 'bar', icon: 'Menu - Drink.jpg' }
  ];
  
  const categoryMap: Record<string, number> = {};
  categories.forEach((cat, idx) => {
    const info = insertCategory.run(cat.name, cat.station, cat.icon, idx);
    categoryMap[cat.name] = info.lastInsertRowid as number;
  });

  // Insert Items
  const insertItem = db.prepare(`INSERT INTO items (name, price, average_make_time, category_id, station, created_at) VALUES (?, ?, ?, ?, ?, ?)`);
  const now = new Date().toISOString();

  const items = [
    // Classic screenshot items
    { name: 'Bia Saigon', price: 15000, time: 2, cat: 'Bia & Đồ uống', station: 'bar' },
    { name: 'Bia Saigon Special', price: 17800, time: 2, cat: 'Bia & Đồ uống', station: 'bar' },
    { name: 'Bò Bít Tết', price: 45000, time: 15, cat: 'Mains', station: 'kitchen' },
    { name: 'Xà Lách', price: 62500, time: 5, cat: 'Starter', station: 'kitchen' },
    { name: 'French Fries', price: 18000, time: 8, cat: 'Starter', station: 'kitchen' },
    { name: 'Bánh Flan', price: 25000, time: 5, cat: 'Dessert', station: 'kitchen' },
    { name: 'Chè Khúc Bạch', price: 35000, time: 6, cat: 'Dessert', station: 'kitchen' },
    
    // Authentic items
    { name: 'Trà Sen', price: 25000, time: 5, cat: 'Trà', station: 'bar' },
    { name: 'Nước Cam', price: 30000, time: 5, cat: 'Nước Trái Cây', station: 'bar' },
    { name: 'Sinh tố Bơ', price: 35000, time: 7, cat: 'Sinh Tố', station: 'bar' },
    { name: 'Coke', price: 15000, time: 2, cat: 'Nước Ngọt', station: 'bar' },
    { name: 'Bánh mì sốt vang', price: 40000, time: 10, cat: 'Ăn sáng', station: 'kitchen' },
    { name: 'Bún riêu cua', price: 35000, time: 10, cat: 'Ăn sáng', station: 'kitchen' },
    { name: 'Café Espresso', price: 25000, time: 5, cat: 'Café', station: 'bar' },
    { name: 'Maganita', price: 50000, time: 5, cat: 'Cocktail', station: 'bar' },
    { name: 'Trà Đào', price: 30000, time: 5, cat: 'Trà', station: 'bar' },
    { name: 'Trà Vải', price: 30000, time: 5, cat: 'Trà', station: 'bar' },
    { name: 'Trà Matcha', price: 35000, time: 5, cat: 'Trà', station: 'bar' },
    { name: 'Nước Ép Táo', price: 35000, time: 5, cat: 'Nước Trái Cây', station: 'bar' },
    { name: 'Nước Ép Dưa Hấu', price: 30000, time: 5, cat: 'Nước Trái Cây', station: 'bar' },
    { name: 'Sinh Tố Xoài', price: 35000, time: 7, cat: 'Sinh Tố', station: 'bar' },
    { name: 'Sinh Tố Dâu', price: 40000, time: 7, cat: 'Sinh Tố', station: 'bar' },
    { name: '7Up', price: 15000, time: 2, cat: 'Nước Ngọt', station: 'bar' },
    { name: 'Phở Bò', price: 45000, time: 10, cat: 'Ăn sáng', station: 'kitchen' },
    { name: 'Bún Bò Huế', price: 45000, time: 10, cat: 'Ăn sáng', station: 'kitchen' },
    { name: 'Cơm Tấm Sườn', price: 40000, time: 12, cat: 'Mains', station: 'kitchen' },
    { name: 'Mì Ý Sốt Bò Băm', price: 55000, time: 15, cat: 'Mains', station: 'kitchen' },
    { name: 'Gỏi Cuốn', price: 20000, time: 5, cat: 'Starter', station: 'kitchen' },
    { name: 'Chả Giò', price: 30000, time: 10, cat: 'Starter', station: 'kitchen' },
    { name: 'Súp Cua', price: 25000, time: 5, cat: 'Starter', station: 'kitchen' },
    { name: 'Kem Vani', price: 20000, time: 2, cat: 'Dessert', station: 'kitchen' },
    { name: 'Kem Socola', price: 20000, time: 2, cat: 'Dessert', station: 'kitchen' },
    { name: 'Trái Cây Dĩa', price: 40000, time: 5, cat: 'Dessert', station: 'kitchen' },
    { name: 'Café Sữa Đá', price: 20000, time: 5, cat: 'Café', station: 'bar' },
    { name: 'Café Đen Đá', price: 18000, time: 5, cat: 'Café', station: 'bar' },
    { name: 'Bạc Xỉu', price: 22000, time: 5, cat: 'Café', station: 'bar' },
    { name: 'Mojito', price: 45000, time: 5, cat: 'Cocktail', station: 'bar' },
    { name: 'Pina Colada', price: 50000, time: 5, cat: 'Cocktail', station: 'bar' },
    { name: 'Virgin Mojito', price: 35000, time: 5, cat: 'Mocktail', station: 'bar' },
    { name: 'Shirley Temple', price: 35000, time: 5, cat: 'Mocktail', station: 'bar' },
    { name: 'Bia Heineken', price: 20000, time: 2, cat: 'Bia & Đồ uống', station: 'bar' },
    { name: 'Bia Tiger', price: 18000, time: 2, cat: 'Bia & Đồ uống', station: 'bar' },
    { name: 'Nước Suối', price: 10000, time: 2, cat: 'Nước Ngọt', station: 'bar' },
    { name: 'Soda Chanh', price: 20000, time: 5, cat: 'Nước Ngọt', station: 'bar' },
    { name: 'Nước Ép Thơm', price: 30000, time: 5, cat: 'Nước Trái Cây', station: 'bar' },
    { name: 'Nước Ép Cà Rốt', price: 30000, time: 5, cat: 'Nước Trái Cây', station: 'bar' },
    { name: 'Trà Gừng', price: 25000, time: 5, cat: 'Trà', station: 'bar' },
    { name: 'Trà Hoa Cúc', price: 30000, time: 5, cat: 'Trà', station: 'bar' },
    { name: 'Mì Xào Hải Sản', price: 50000, time: 15, cat: 'Mains', station: 'kitchen' },
    { name: 'Cơm Chiên Dương Châu', price: 45000, time: 15, cat: 'Mains', station: 'kitchen' },
    { name: 'Gà Rán', price: 35000, time: 12, cat: 'Mains', station: 'kitchen' },
    { name: 'Bò Lúc Lắc', price: 60000, time: 15, cat: 'Mains', station: 'kitchen' },
    { name: 'Salad Cá Ngừ', price: 45000, time: 8, cat: 'Starter', station: 'kitchen' },
    { name: 'Khoai Tây Chiên', price: 25000, time: 8, cat: 'Starter', station: 'kitchen' },
    { name: 'Súp Bắp Cua', price: 30000, time: 5, cat: 'Starter', station: 'kitchen' },
    { name: 'Bánh Mì', price: 5000, time: 2, cat: 'Ăn sáng', station: 'kitchen' },
    { name: 'Hủ Tiếu Nam Vang', price: 45000, time: 10, cat: 'Ăn sáng', station: 'kitchen' },
    { name: 'Cơm Tấm Chả Cua', price: 45000, time: 12, cat: 'Mains', station: 'kitchen' },
    { name: 'Lẩu Thái', price: 150000, time: 20, cat: 'Mains', station: 'kitchen' },
    { name: 'Sữa Chua Trái Cây', price: 30000, time: 5, cat: 'Dessert', station: 'kitchen' },
    { name: 'Panna Cotta', price: 40000, time: 5, cat: 'Dessert', station: 'kitchen' }
  ];

  const itemMap: Record<string, number> = {};
  items.forEach(item => {
    const info = insertItem.run(item.name, item.price, item.time, categoryMap[item.cat], item.station, now);
    itemMap[item.name] = info.lastInsertRowid as number;
  });

  // Helper to create an order
  let ticketNumber = 1;
  const insertOrder = db.prepare(`INSERT INTO orders (ticket_number, table_id, user_id, source, status, tax_rate, discount_rate, created_at, started_at, ready_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`);
  const insertOrderItem = db.prepare(`INSERT INTO order_items (order_id, item_id, item_name, quantity, unit_price, prep_time_min, station, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)`);

  const createDemoOrder = (tableId: number, status: string, itemNames: string[], offsetMinutes: number) => {
    const orderCreatedAt = new Date(Date.now() - offsetMinutes * 60000).toISOString();
    const startedAt = status !== 'queued' ? new Date(Date.now() - (offsetMinutes - 1) * 60000).toISOString() : null;
    const readyAt = status === 'ready' || status === 'served' ? new Date(Date.now() - (offsetMinutes - 10) * 60000).toISOString() : null;
    
    const info = insertOrder.run(ticketNumber++, tableId, 1, 'waiter', status, 10, 0, orderCreatedAt, startedAt, readyAt);
    const orderId = info.lastInsertRowid as number;

    itemNames.forEach(name => {
      const itemDef = items.find(i => i.name === name)!;
      insertOrderItem.run(orderId, itemMap[name], name, 1, itemDef.price, itemDef.time, itemDef.station, status);
    });
  };

  // Table 1: Tea/Breakfast in preparing (let's say 5 mins ago)
  createDemoOrder(1, 'preparing', ['Trà Sen', 'Bánh mì sốt vang'], 5);

  // Table 2: requested bill (orders served 30 mins ago)
  createDemoOrder(2, 'served', ['Cơm Tấm Sườn', 'Mì Ý Sốt Bò Băm', 'Nước Cam', 'Coke'], 30);

  // Table 5: Bia Saigon, Bia Saigon Special, Bò Bít Tết, Xà Lách, French Fries (active orders, some ready, some preparing)
  createDemoOrder(5, 'preparing', ['Bò Bít Tết', 'Xà Lách', 'French Fries'], 8);
  createDemoOrder(5, 'served', ['Bia Saigon', 'Bia Saigon Special'], 10);
}
