create table reroute_token
(
    id        bigint auto_increment
        primary key,
    created   datetime(6)  null,
    email     varchar(255) not null,
    timeout   datetime(6)  null,
    parcel_id bigint       not null,
    token     int          not null
);