package com.warehouse.zebrareturn.infrastructure.adapter.secondary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelDto {

    Long id;

    SizeDto parcelSize;

    String destination;

    StatusDto status;

    ParcelTypeDto parcelType;

    Long parcelRelatedId;
}
