package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelDto {
    Long id;
    Long parcelRelatedId;
    ParcelStatusDto parcelStatus;
    Boolean locked;
}
