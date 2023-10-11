package com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.dto;

import lombok.Value;

@Value
public class DeliveryPackageRequestDto {
    DeliveryDto delivery;
    SupplierDto supplier;
    ParcelIdDto parcelId;
}
