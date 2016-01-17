CREATE TABLE users (
	id integer NOT NULL,
	username text,
	password varchar,
	business_id integer,
	role_id integer,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE businesses (
	id integer NOT NULL,
	name varchar,
	address text,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE tables (
	id integer NOT NULL,
	business_id integer,
	name varchar,
	status integer,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE roles (
	id integer NOT NULL,
	name varchar,
	type varchar,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE orders (
	id integer NOT NULL,
	user_id integer,
	table_id integer,
	item_id integer,
	item_quantity integer,
	status integer,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE items (
	id integer NOT NULL,
	name varchar,
	average_make_time integer,
	average_make_time_unit varchar,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE order_status (
	id integer NOT NULL,
	name varchar,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);

CREATE TABLE table_status (
	id integer NOT NULL,
	name varchar,
	created datetime,
	modified datetime,
	PRIMARY KEY (id)
);
