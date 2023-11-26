package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder
@Jacksonized
public class RouteRequestDto {
    Long parcelId;
    String username;
    String supplierCode;
    String depotCode;
}
