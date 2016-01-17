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