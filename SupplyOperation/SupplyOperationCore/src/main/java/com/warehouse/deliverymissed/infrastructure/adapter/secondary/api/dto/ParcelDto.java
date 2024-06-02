package com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelDto {

    ParcelIdDto parcelId;

    StatusDto parcelStatus;

}
