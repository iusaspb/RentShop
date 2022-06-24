DROP DOMAIN IF EXISTS table_id CASCADE;
CREATE DOMAIN table_id AS integer;

DROP TABLE IF EXISTS category CASCADE;
CREATE TABLE category (
	id SERIAL,
	"name" varchar NOT NULL,
	parent_id table_id NULL,
	CONSTRAINT category_pk PRIMARY KEY (id),
	CONSTRAINT category_fk FOREIGN KEY (parent_id) REFERENCES category(id)
);


INSERT INTO category ( "name", parent_id) VALUES( 'root', null);
INSERT INTO category ( "name", parent_id) SELECT 'c1', id FROM category WHERE parent_id is null;
INSERT INTO category ( "name", parent_id) SELECT 'c2', id FROM category WHERE parent_id is null;


DROP TABLE IF EXISTS "owner" CASCADE;
CREATE TABLE "owner" (
	id SERIAL,
	latitude real,
	longitude real,
	"name" varchar NOT NULL,
	CONSTRAINT owner_pk PRIMARY KEY (id)
);


INSERT INTO "owner" (latitude, longitude, "name") VALUES( 0.1, 0.2, 'owner');


DROP TABLE IF EXISTS contractor CASCADE;
CREATE TABLE contractor (
	id SERIAL,
	latitude real,
	longitude real,
	"name" varchar NOT NULL,
	CONSTRAINT contractor_pk PRIMARY KEY (id)
);

INSERT INTO contractor (latitude, longitude, "name") VALUES( 1.1, 1.2, 'contractor');


DROP TABLE IF EXISTS pickup_center;
CREATE TABLE pickup_center (
	id SERIAL,
	latitude float4,
	longitude float4,
	"name" varchar NOT NULL,
	CONSTRAINT pickup_center_pk PRIMARY KEY (id)
);

INSERT INTO pickup_center (latitude, longitude, "name") VALUES( 0.3, 0.4, 'pickup_center');

DROP TABLE IF EXISTS product CASCADE;
CREATE TABLE product (
	id SERIAL,
	"name" varchar NOT NULL,
	description varchar NOT NULL,
	brand varchar NOT NULL,
	category_id table_id NOT NULL,
	owner_id table_id NOT NULL,
	price int4 NOT NULL,
	updated timestamp NOT NULL DEFAULT now(),
	CONSTRAINT product_pk PRIMARY KEY (id)
);


ALTER TABLE product ADD CONSTRAINT product_category_fk FOREIGN KEY (category_id) REFERENCES category(id);
ALTER TABLE product ADD CONSTRAINT product_owner_fk FOREIGN KEY (owner_id) REFERENCES "owner"(id);


INSERT INTO product ("name", description, brand, category_id, owner_id, price)
SELECT 'prodcut1', 'description1', 'brand1', category.id as category_id, "owner".id as owner_id,100 FROM  "owner", category where category."name" ='c1' ;


SELECT id FROM  "owner" ;

DROP TABLE IF EXISTS item CASCADE;
CREATE TABLE item (
	id SERIAL,
	product_id table_id NOT NULL,
	pickup_center_id table_id,
	serial_num varchar,
	available_intervals varchar NOT NULL,
	available_intervals_react varchar[] NOT NULL,
	"version" int4 NOT NULL DEFAULT 0,
	CONSTRAINT item_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN item.available_intervals_react IS 'It is used in react case';
COMMENT ON COLUMN item.available_intervals IS 'it is used in jpa case';


ALTER TABLE item ADD CONSTRAINT item_product_fk FOREIGN KEY (product_id) REFERENCES product(id);
ALTER TABLE item ADD CONSTRAINT item_pickup_center_fk FOREIGN KEY (pickup_center_id) REFERENCES pickup_center(id);


INSERT INTO item (product_id, available_intervals,available_intervals_react)
SELECT id as product_id, '2022-01-01;2022-12-30', '{2022-01-01;2022-12-30}' FROM  product ;


DROP TABLE IF EXISTS "order" CASCADE;
CREATE TABLE "order" (
	id SERIAL,
	contractor_id table_id NOT NULL,
	pickup_center_id table_id,
	amount int4 NOT NULL DEFAULT 0,
	status varchar NOT NULL,
	updated timestamp NOT NULL DEFAULT now(),
	"version" int4 NOT NULL DEFAULT 0,
	CONSTRAINT order_pk PRIMARY KEY (id)
);


ALTER TABLE "order" ADD CONSTRAINT order_contractor_fk FOREIGN KEY (contractor_id) REFERENCES contractor(id);
ALTER TABLE "order" ADD CONSTRAINT order_pickup_center_fk FOREIGN KEY (pickup_center_id) REFERENCES pickup_center(id);


DROP TABLE IF EXISTS order_hist;
create table order_hist as select 'UPDATE':: char(10) as oper, * from "order" with no data;
ALTER TABLE order_hist ADD hist_id serial4 NOT NULL;

ALTER TABLE order_hist ADD CONSTRAINT order_hist_pk PRIMARY KEY (hist_id);
ALTER TABLE order_hist ADD CONSTRAINT order_hist_order_fk FOREIGN KEY (id) REFERENCES "order"(id)  ON DELETE CASCADE;


CREATE or REPLACE FUNCTION order_hist_tf() RETURNS trigger AS $order_hist_tf$
    BEGIN
	    INSERT INTO order_hist VALUES(TG_OP, NEW.*);
        RETURN NEW;
    END;
$order_hist_tf$ LANGUAGE plpgsql;


CREATE or REPLACE TRIGGER order_hist_tr AFTER INSERT or UPDATE ON "order"
    FOR EACH ROW
    EXECUTE PROCEDURE order_hist_tf();


DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
	id SERIAL,
	order_id table_id NOT NULL,
	item_id table_id NOT NULL,
	price int4 NOT NULL,
	rent_period varchar NOT NULL,
	updated timestamp NOT NULL DEFAULT now(),
	CONSTRAINT order_item_pk PRIMARY KEY (id)
);

ALTER TABLE order_item ADD CONSTRAINT order_item_item_fk FOREIGN KEY (item_id) REFERENCES item(id);
ALTER TABLE order_item ADD CONSTRAINT order_item_order_fk FOREIGN KEY (order_id) REFERENCES "order"(id) ON DELETE CASCADE;

