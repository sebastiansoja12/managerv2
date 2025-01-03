create table reject_reason
(
    id                     bigint auto_increment
        primary key,
    reason                 varchar(255) null,
    recipient              varchar(255) null,
    reject_department_code varchar(255) null,
    shipment_id            bigint       null
);