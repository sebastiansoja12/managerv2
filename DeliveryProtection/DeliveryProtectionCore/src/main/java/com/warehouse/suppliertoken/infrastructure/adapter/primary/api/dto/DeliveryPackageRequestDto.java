package com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

@Value
public class DeliveryPackageRequestDto {
    DeliveryDto delivery;
    SupplierDto supplier;
    ParcelIdDto parcelId;
}
