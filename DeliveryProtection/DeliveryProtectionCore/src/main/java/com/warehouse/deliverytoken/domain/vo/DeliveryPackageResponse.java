package com.warehouse.deliverytoken.domain.vo;

import lombok.Value;

@Value
public class DeliveryPackageResponse {
    Parcel parcel;
    String supplierTokenServiceApplicationId;
    ProtectedDelivery protectedDelivery;
}
