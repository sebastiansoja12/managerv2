package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReturnTrackRequestDto {
    Long parcelId;
    ProcessTypeDto processType;
    String username;
    String depotCode;
}