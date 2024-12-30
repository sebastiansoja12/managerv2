create table returning_information
(
    id            bigint       not null
        primary key,
    reason        varchar(255) not null,
    return_status tinyint      not null,
    check (`return_status` between 0 and 3)
);

create table returning
(
    id                       bigint auto_increment
        primary key,
    depot_code               varchar(255) not null,
    parcel_id                bigint       not null,
    return_status            tinyint      not null,
    return_token             varchar(255) not null,
    supplier_code            varchar(255) not null,
    username                 varchar(255) not null,
    returning_information_id bigint       null,
    constraint UK_dp3jby0ov8ww1f2newyq158v4
        unique (returning_information_id),
    constraint FKqladspg7q9xlsoo2pqcpy6n5t
        foreign key (returning_information_id) references returning_information (id),
    check (`return_status` between 0 and 3)
);

create table returning_information_seq
(
    next_val bigint null
);