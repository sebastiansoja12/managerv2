package com.warehouse.routetracker.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RouteDeleteRequest {
    String id;

    Long parcelId;

    String username;
}
