package com.warehouse.routetracker.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.NonNull;
@Builder
public class ParcelStatusRequestDto {

    @NonNull
    Long parcelId;
    String status;
}
