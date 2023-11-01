package com.warehouse.deliverytoken.domain.model;

import lombok.Value;

@Value
public class DeliveryPackageRequest {
    Parcel parcel;
    Delivery delivery;
}
