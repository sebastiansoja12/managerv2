
CREATE TABLE IF NOT EXISTS ROUTE (
id varchar(255) NOT NULL,
created datetime DEFAULT NULL,
depot_id bigint DEFAULT NULL,
parcel_id bigint DEFAULT NULL,
supplier_id bigint DEFAULT NULL,
user_id int DEFAULT NULL,
primary key(id)
);
CREATE TABLE IF NOT EXISTS USERS (
id int NOT NULL,
email varchar(255) NOT NULL,
first_name varchar(255) DEFAULT NULL,
last_name varchar(255) DEFAULT NULL,
password varchar(255) NOT NULL,
role varchar(255) DEFAULT NULL,
username varchar(255) NOT NULL,
depot_id bigint DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS PAYMENT (
id bigint NOT NULL,
amount double NOT NULL,
parcel_status int DEFAULT NULL,
payment_pass int DEFAULT NULL,
payment_url varchar(255) DEFAULT NULL,
paypal_id varchar(255) DEFAULT NULL,
parcel_id varchar(255) DEFAULT NULL
);
CREATE TABLE IF NOT EXISTS REROUTE_TOKEN (
id bigint NOT NULL,
created_date datetime DEFAULT NULL,
timeout datetime DEFAULT NULL,
token bigint DEFAULT NULL,
parcel_id bigint DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS PARCEL (
id bigint NOT NULL,
first_name varchar(255) DEFAULT NULL,
last_name varchar(255) DEFAULT NULL,
sender_email varchar(255) DEFAULT NULL,
sender_city varchar(255) DEFAULT NULL,
sender_street varchar(255) DEFAULT NULL,
sender_postal_code varchar(255) DEFAULT NULL,
recipient_city varchar(255) DEFAULT NULL,
recipient_email varchar(255) DEFAULT NULL,
recipient_first_name varchar(255) DEFAULT NULL,
recipient_last_name varchar(255) DEFAULT NULL,
recipient_postal_code varchar(255) DEFAULT NULL,
recipient_street varchar(255) DEFAULT NULL,
recipient_telephone varchar(255) DEFAULT NULL,
price DOUBLE NOT NULL,
parcel_size varchar(255) DEFAULT NULL,
primary key (id)
);
