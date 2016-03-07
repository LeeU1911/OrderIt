INSERT INTO businesses (rowid, id, name, address, created, modified) VALUES (null, null, 'restaurant1', 'Cong Hoa', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');


INSERT INTO roles (rowid, id, name, type, created, modified) VALUES (null, null, 'waiter', 'staff', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');

INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 1', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 2', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 3', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 4', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 5', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 6', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 7', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 8', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 9', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');
INSERT INTO tables (rowid, id, business_id, name, created, modified) VALUES (null, null, 1, 'table 10', '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');

INSERT INTO users(id, username, password, business_id, role_id, created, modified) VALUES (1, 'staff1', 'password', 1, 1, '2015-12-21 21:17:00.000', '2015-12-21 21:17:00.000');


INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Trà ', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Nước Trái Cây ', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Sinh Tố', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Nước Ngọt', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Ăn sáng', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Mocktail', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Café', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO categories (rowid, id, name, created, modified) VALUES (null, null, 'Cocktail', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');

INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Sen	',	 20000 	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Nhài	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Bạc Hà	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Đào	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Dâu	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Lipton	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Hoa cúc mật ong	',	38000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Mạn	',	20000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Sen Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Nhài Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Bạc Hà Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Đào Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Dâu Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Lipton Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Hoa cúc mật ong Sữa	',	40000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Trà Mạn Sữa	',	22000	 , 'VND', 10, 'min', 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Cam	',	52000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Cam - Cà rốt	',	52000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Chanh tươi	',	25000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Chanh leo	',	35000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước ổi	',	40000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Dứa	',	40000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Xoài	',	35000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Dưa hấu	',	32000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Táo	',	55000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Cà rốt	',	40000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Bưởi	',	58000	 , 'VND', 10, 'min', 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 		


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Bơ	',	50000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Bơ + Mãng Cầu	',	45000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Mãng Cầu	',	40000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Xoài	',	35000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Đu Đủ	',	50000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Dâu	',	40000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sinh tố Dưa hấu	',	35000	 , 'VND', 10, 'min', 3, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Coke	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Spite	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Fanta	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Twister	',	20000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Redbull	',	25000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sting	',	25000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nước Suối	',	15000	 , 'VND', 10, 'min', 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 	


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Bánh mì sốt vang	',	30000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Bún riêu cua	',	25000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Bún riêu cua đậu phụ	',	30000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Bún riêu cua đậu phụ bắp bò	',	40000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Bún riêu cua đậu phụ giò tai	',	35000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Cháo sụn ngô non	',	39000	 , 'VND', 10, 'min', 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Nắng Vàng Hawai	',	45000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Virgin Canada	',	45000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Honey Moon	',	69000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Angle Smile	',	59000	 , 'VND', 10, 'min', 6, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Café Espresso	',	30000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Espresso Doppio	',	45000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Café Latte	',	45000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Café Cappuccino	',	45000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Molstar Coffee	',	55000	 , 'VND', 10, 'min', 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Maganita	',	68000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	I love you	',	95000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Lady Japanese	',	99000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Sex on the beach	',	85000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Black Russia	',	65000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 
INSERT INTO items (rowid, id, name, price, price_unit, average_make_time, average_make_time_unit, category_id, created, modified) VALUES (null, null, '	Strawberry	',	85000	 , 'VND', 10, 'min', 8, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000'); 


INSERT INTO order_status (rowid, id, name, created, modified) VALUES (null, null, 'new', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_status (rowid, id, name, created, modified) VALUES (null, null, 'order', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_status (rowid, id, name, created, modified) VALUES (null, null, 'paid', '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');

INSERT INTO orders (rowid, id, user_id, table_id, status, created, modified) VALUES (null, null, 1, 1, 1, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');

INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 1, 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 2, 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 5, 2, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 45, 4, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 40, 5, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 35, 10, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 23, 21, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
INSERT INTO order_details (rowid, id, order_id, item_id, item_quantity, created, modified) VALUES (null, null, 1, 56, 7, '2016-03-03 21:17:00.000', '2016-03-03 21:17:00.000');
