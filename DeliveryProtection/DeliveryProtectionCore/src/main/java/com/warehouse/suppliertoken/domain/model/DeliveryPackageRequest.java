package com.warehouse.suppliertoken.domain.model;

import lombok.Value;

@Value
public class DeliveryPackageRequest {
    Parcel parcel;
    Supplier supplier;
    Delivery delivery;
}
