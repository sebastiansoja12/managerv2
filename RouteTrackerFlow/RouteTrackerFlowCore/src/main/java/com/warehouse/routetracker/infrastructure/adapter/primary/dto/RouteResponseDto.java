package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RouteResponseDto {
    UUID id;
    Long parcelId;
}
