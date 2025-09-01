
create table delivery
(
    id              varchar(255) not null
        primary key,
    created         datetime(6)  not null,
    delivery_status tinyint      not null,
    depot_code varchar(255) not null,
    parcel_id     bigint       not null,
    supplier_code   varchar(255) not null,
    token           varchar(255) not null,
    check (`delivery_status` between 0 and 8)
);

create table depot
(
    depot_code  varchar(255) not null
        primary key,
    city             varchar(255) not null,
    country          varchar(255) not null,
    street           varchar(255) not null,
    nip              varchar(255) not null,
    opening_hours    varchar(255) not null,
    postal_code      varchar(255) not null,
    telephone_number varchar(255) not null
);

create table message_sequence
(
    next_val bigint null
);

create table redirect_token
(
    id        bigint auto_increment
        primary key,
    created   datetime(6)  null,
    email     varchar(255) not null,
    timeout   datetime(6)  null,
    parcel_id bigint       not null,
    token     varchar(255) not null
);

create table parcel
(
    id                    bigint       not null
        primary key,
    created_at            datetime(6)  not null,
    destination           varchar(255) not null,
    first_name            varchar(255) not null,
    last_name             varchar(255) not null,
    locked                bit          not null,
    parcel_related_id     bigint       null,
    parcel_size           varchar(20)     not null,
    type                  varchar(7)      not null,
    recipient_city        varchar(255) not null,
    recipient_email       varchar(255) not null,
    recipient_first_name  varchar(255) not null,
    recipient_last_name   varchar(255) not null,
    recipient_postal_code varchar(255) not null,
    recipient_street      varchar(255) not null,
    recipient_telephone   varchar(255) not null,
    sender_city           varchar(255) not null,
    sender_email          varchar(255) not null,
    sender_postal_code    varchar(255) not null,
    sender_street         varchar(255) not null,
    sender_telephone      varchar(255) not null,
    status                varchar(10)      not null,
    updated_at            datetime(6)  not null
);

create table supplier
(
    supplier_code varchar(255) not null
        primary key,
    active        bit          not null,
    first_name    varchar(255) not null,
    last_name     varchar(255) not null,
    telephone     varchar(255) not null,
    depot_code    varchar(255) null,
    constraint FKbxmw0i9gg06jh9dx8a1cnck7n
        foreign key (depot_code) references depot (depot_code)
);

create table users
(
    username        varchar(255) not null
        primary key,
    depot_code      varchar(255) not null,
    email           varchar(255) not null,
    first_name      varchar(255) not null,
    last_name       varchar(255) not null,
    password        varchar(255) not null,
    role            tinyint      not null,
    id              bigint       not null,
    check (`role` between 0 and 2)
);
