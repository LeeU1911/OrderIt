CREATE TABLE users (
	id serial NOT NULL,
	username text,
	password varchar,
	email varchar,
	business_id integer,
	role_id integer,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE businesses (
	id serial NOT NULL,
	name varchar,
	address text,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE tables (
	id serial NOT NULL,
	business_id integer,
	name varchar,
	status integer,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE roles (
	id serial NOT NULL,
	name varchar,
	type varchar,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE orders (
	id serial NOT NULL,
	user_id integer,
	table_id integer,
	status integer,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE items (
	id serial NOT NULL,
	name varchar,
	price integer,
	price_unit varchar,
	average_make_time integer,
	average_make_time_unit varchar,
	category_id integer,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE order_status (
	id serial NOT NULL,
	name varchar,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE table_status (
	id serial NOT NULL,
	name varchar,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE categories (
	id serial,
	name varchar,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE order_details (
	id serial,
	order_id integer,
	item_id integer,
	item_quantity integer,
	created timestamp,
	modified timestamp,
	PRIMARY KEY (id)
);