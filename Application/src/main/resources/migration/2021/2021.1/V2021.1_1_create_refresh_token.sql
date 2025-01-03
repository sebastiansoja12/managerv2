create table refresh_token
(
    id           bigint auto_increment
        primary key,
    created_date datetime(6)  not null,
    expired      bit          not null,
    expiry_date  datetime(6)  not null,
    revoked      bit          not null,
    token        varchar(255) not null,
    username     varchar(255) not null,
    constraint UK_r4k4edos30bx9neoq81mdvwph
        unique (token)
);
