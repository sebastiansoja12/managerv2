create table device_pair
(
    device_pair_id    bigint       not null
        primary key,
    error_description varchar(255) null,
    pair_time         datetime(6)  null,
    pair_key          varchar(255) null,
    paired            bit          null,
    device_id         bigint       null,
    constraint UK1atq2sgsooux0v0xkdimfg57
        unique (device_id),
    constraint FK1cg0qo6bp0qb5qfoy0emmt112
        foreign key (device_id) references device (device_id)
);