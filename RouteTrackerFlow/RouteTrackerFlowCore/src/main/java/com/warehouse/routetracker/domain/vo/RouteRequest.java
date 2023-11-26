package com.warehouse.routetracker.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RouteRequest {
    Long parcelId;
    String username;
    String supplierCode;
    String depotCode;
}
