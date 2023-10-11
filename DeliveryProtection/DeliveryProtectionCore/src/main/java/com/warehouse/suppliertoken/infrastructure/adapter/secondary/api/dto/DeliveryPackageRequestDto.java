package com.warehouse.suppliertoken.infrastructure.adapter.secondary.api.dto;

import lombok.Value;

@Value
public class DeliveryPackageRequestDto {
    DeliveryDto delivery;
    SupplierDto supplier;
    ParcelIdDto parcelId;
}
