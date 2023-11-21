package com.warehouse.delivery.infrastructure.adapter.secondary.api;

import lombok.Value;

@Value
public class ParcelDto {
    ParcelIdDto parcelId;
    StatusDto status;
}
