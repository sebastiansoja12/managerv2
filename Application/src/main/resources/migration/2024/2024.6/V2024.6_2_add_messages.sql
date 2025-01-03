create table messages
(
    id              bigint auto_increment
        primary key,
    title           varchar(255)                                                          not null,
    shipment_status enum ('CREATED', 'REROUTE', 'SENT', 'DELIVERY', 'RETURN', 'REDIRECT') not null,
    language        varchar(255)                                                          not null,
    message_content tinytext                                                              not null,
    created_at      datetime                                                              not null,
    updated_at      datetime                                                              null,
    sender          varchar(255)                                                          null
);