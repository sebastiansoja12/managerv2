package com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

@Value
public class DeliveryPackageRequestDto {
    DeliveryDto delivery;
    ParcelIdDto parcelId;
}
