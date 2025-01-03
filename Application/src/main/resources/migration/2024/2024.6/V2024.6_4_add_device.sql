create table device
(
    device_id       bigint                                   not null
        primary key,
    active          bit                                      not null,
    depot_code varchar(255)                             not null,
    device_type     enum ('MANAGER', 'SUPPLIER', 'TERMINAL') not null,
    last_update     datetime(6)                              not null,
    user_id         bigint                                   not null,
    version         varchar(255)                             not null
);