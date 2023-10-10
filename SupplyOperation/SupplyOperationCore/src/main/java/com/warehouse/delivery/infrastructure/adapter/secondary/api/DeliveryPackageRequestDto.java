package com.warehouse.delivery.infrastructure.adapter.secondary.api;

import lombok.Value;

@Value
public class DeliveryPackageRequestDto {
    DeliveryDto delivery;
    SupplierDto supplier;
    ParcelIdDto parcelId;
}
