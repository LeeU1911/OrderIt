INSERT INTO businesses (name, address, created, modified) VALUES ('restaurant1', 'Cong Hoa', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');


INSERT INTO roles (name, type, created, modified) VALUES ('waiter', 'staff', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');

INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 1', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 2', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 3', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 4', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 5', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 6', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 7', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 8', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 9', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (business_id, name, created, modified) VALUES (1, 'table 10', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');

INSERT INTO users(username, password, email, business_id, role_id, created, modified) VALUES ('staff1', 'password', 'staff@business.com', 1, 1, '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');


INSERT INTO categories (name, created, modified) VALUES ('Trà ', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Nước Trái Cây ', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Sinh Tố', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Nước Ngọt', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Ăn sáng', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Mocktail', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Café', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (name, created, modified) VALUES ('Cocktail', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');

INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Sen	',	 20000 	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Nhài	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Bạc Hà	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Đào	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Dâu	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Lipton	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Hoa cúc mật ong	',	38000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Mạn	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Sen Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Nhài Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Bạc Hà Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Đào Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Dâu Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Lipton Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Hoa cúc mật ong Sữa	',	40000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Trà Mạn Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Cam	',	52000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Cam - Cà rốt	',	52000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Chanh tươi	',	25000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Chanh leo	',	35000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước ổi	',	40000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Dứa	',	40000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Xoài	',	35000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Dưa hấu	',	32000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Táo	',	55000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Cà rốt	',	40000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Bưởi	',	58000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Bơ	',	50000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Bơ + Mãng Cầu	',	45000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Mãng Cầu	',	40000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Xoài	',	35000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Đu Đủ	',	50000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Dâu	',	40000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sinh tố Dưa hấu	',	35000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Coke	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Spite	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Fanta	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Twister	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Redbull	',	25000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sting	',	25000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nước Suối	',	15000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Bánh mì sốt vang	',	30000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Bún riêu cua	',	25000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Bún riêu cua đậu phụ	',	30000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Bún riêu cua đậu phụ bắp bò	',	40000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Bún riêu cua đậu phụ giò tai	',	35000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Cháo sụn ngô non	',	39000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Nắng Vàng Hawai	',	45000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Virgin Canada	',	45000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Honey Moon	',	69000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Angle Smile	',	59000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Café Espresso	',	30000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Espresso Doppio	',	45000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Café Latte	',	45000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Café Cappuccino	',	45000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Molstar Coffee	',	55000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Maganita	',	68000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	I love you	',	95000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Lady Japanese	',	99000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Sex on the beach	',	85000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Black Russia	',	65000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES ('	Strawberry	',	85000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO order_status (name, created, modified) VALUES ('new', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_status (name, created, modified) VALUES ('order', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_status (name, created, modified) VALUES ('paid', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');

INSERT INTO orders (user_id, table_id, status, created, modified) VALUES (1, 1, 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');

INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 1, 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 2, 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 5, 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 45, 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 40, 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 35, 10, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 23, 21, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (order_id, item_id, item_quantity, created, modified) VALUES (1, 56, 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
