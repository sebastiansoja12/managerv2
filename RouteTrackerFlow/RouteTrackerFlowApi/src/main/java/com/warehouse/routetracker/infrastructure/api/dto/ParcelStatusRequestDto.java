package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.NonNull;
@Builder
public class ParcelStatusRequestDto {

    @NonNull
    Long parcelId;
    String status;
}
