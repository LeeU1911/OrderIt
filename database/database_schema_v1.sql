CREATE TABLE users (
	id integer,
	username text,
	password varchar,
	business_id integer,
	role_id integer,
	created datetime,
	modified datetime
);

CREATE TABLE businesses (
	id integer,
	name varchar,
	address text,
	created datetime,
	modified datetime
);

CREATE TABLE tables (
	id integer,
	business_id integer,
	name varchar,
	created datetime,
	modified datetime
);

CREATE TABLE roles (
	id integer,
	name varchar,
	type varchar,
	created datetime,
	modified datetime
);

CREATE TABLE orders (
	id integer,
	user_id integer,
	table_id integer,
	item_id integer,
	item_quantity integer,
	status integer,
	created datetime,
	modified datetime
);

CREATE TABLE items (
	id integer,
	name varchar,
	average_make_time integer,
	average_make_time_unit varchar,
	created datetime,
	modified datetime
);

CREATE TABLE order_status (
	id integer,
	name varchar,
	created datetime,
	modified datetime
);

