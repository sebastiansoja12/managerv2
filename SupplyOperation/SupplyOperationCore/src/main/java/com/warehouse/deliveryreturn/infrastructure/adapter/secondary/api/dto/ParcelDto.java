package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import lombok.Value;

@Value
public class ParcelDto {
    ParcelIdDto parcelId;
    StatusDto status;
}
