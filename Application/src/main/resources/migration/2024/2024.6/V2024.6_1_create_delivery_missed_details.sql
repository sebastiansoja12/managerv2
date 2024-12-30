create table delivery_missed_details
(
    delivery_missed_detail_id int            not null
        primary key,
    planned_delivery_date     timestamp      not null,
    delivery_date             timestamp      null,
    created                   timestamp      not null,
    new_proposed_date         timestamp      null,
    address_changed           tinyint(1)     not null,
    delivery_attempt_number   int            not null,
    penalty_fee               decimal(19, 4) null,
    suggested_action          varchar(500)   null,
    incident_report           varchar(2000)  null,
    shipment_id               bigint         null
);