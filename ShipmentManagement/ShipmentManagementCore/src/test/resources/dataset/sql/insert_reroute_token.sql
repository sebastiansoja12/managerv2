CREATE TABLE IF NOT EXISTS REROUTE_TOKEN (
                                             id bigint NOT NULL,
                                             created_date varchar(255) DEFAULT NULL,
                                             expiry_date varchar(255) DEFAULT NULL,
                                             token bigint DEFAULT NULL,
                                             parcel_id bigint DEFAULT NULL
);
INSERT INTO REROUTE_TOKEN (ID, TOKEN, PARCEL_ID, CREATED_DATE, EXPIRY_DATE)
VALUES (1, 12345, 100001, '2020-08-10 20:03:10', '2020-08-10 20:13:10')