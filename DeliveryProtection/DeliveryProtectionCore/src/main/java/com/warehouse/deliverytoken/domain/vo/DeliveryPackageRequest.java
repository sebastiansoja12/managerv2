package com.warehouse.deliverytoken.domain.vo;

import lombok.Value;

@Value
public class DeliveryPackageRequest {
    Parcel parcel;
    Delivery delivery;
}
