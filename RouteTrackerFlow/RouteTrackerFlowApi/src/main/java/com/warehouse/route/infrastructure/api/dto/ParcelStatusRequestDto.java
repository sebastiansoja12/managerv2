package com.warehouse.route.infrastructure.api.dto;

import lombok.Builder;
import lombok.NonNull;
@Builder
public class ParcelStatusRequestDto {

    @NonNull
    Long parcelId;
    String status;
}
