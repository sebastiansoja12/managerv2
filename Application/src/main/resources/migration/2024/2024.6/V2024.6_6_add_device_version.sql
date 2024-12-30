create table device_version
(
    id          bigint                                   not null
        primary key,
    device_id   bigint                                   not null,
    device_type enum ('MANAGER', 'SUPPLIER', 'TERMINAL') not null,
    last_update datetime(6)                              not null,
    version     varchar(255)                             not null
);