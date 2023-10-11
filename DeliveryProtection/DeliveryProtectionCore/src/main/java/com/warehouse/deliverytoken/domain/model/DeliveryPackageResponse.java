package com.warehouse.deliverytoken.domain.model;

import lombok.Value;

@Value
public class DeliveryPackageResponse {
    Parcel parcel;
    String supplierCode;
    String supplierTokenServiceApplicationId;
    ProtectedDelivery protectedDelivery;
}
