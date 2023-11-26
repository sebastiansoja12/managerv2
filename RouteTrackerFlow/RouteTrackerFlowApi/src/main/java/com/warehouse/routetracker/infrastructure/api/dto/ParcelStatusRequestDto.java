package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelStatusRequestDto {

    @NonNull
    Long parcelId;
    String status;
}
