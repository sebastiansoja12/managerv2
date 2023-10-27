package com.warehouse.delivery.domain.model;


import lombok.Value;

@Value
public class DeliveryPackageRequest {
    Long parcelId;
    Supplier supplier;
    Delivery delivery;
}
