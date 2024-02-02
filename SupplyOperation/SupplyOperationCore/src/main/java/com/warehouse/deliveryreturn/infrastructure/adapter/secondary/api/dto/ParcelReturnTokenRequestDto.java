package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelReturnTokenRequestDto {
    Long id;
    Long parcelRelatedId;
    String parcelStatus;
    Boolean locked;
}
