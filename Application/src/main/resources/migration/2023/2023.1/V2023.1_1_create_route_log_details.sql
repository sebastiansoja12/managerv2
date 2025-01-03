create table route_log
(
    id                varchar(255) not null
        primary key,
    fault_description varchar(255) null,
    parcel_id         bigint       not null,
    return_code       varchar(255) null
);


create table route_log_details
(
    id                  bigint auto_increment
        primary key,
    created             datetime(6)  null,
    description         varchar(255) null,
    parcel_status       tinyint      null,
    process_type        tinyint      null,
    request             mediumtext   null,
    version             varchar(255) null,
    zebra_id            bigint       null,
    depot_code          varchar(255) null,
    supplier_code       varchar(255) null,
    username            varchar(255) null,
    route_log_record_id varchar(255) null,
    constraint FK46eim8q7kt737b80qhlkx14hx
        foreign key (username) references users (username),
    constraint FKavw5j2hrrok75rhockd1ja5du
        foreign key (route_log_record_id) references route_log (id),
    constraint FKfffgbiisri5i085yd53qe9w9y
        foreign key (depot_code) references depot (depot_code),
    constraint FKkecagav32vak9wj3dlqfobtkk
        foreign key (supplier_code) references supplier (supplier_code),
    check (`parcel_status` between 0 and 5),
    check (`process_type` between 0 and 6)
);
