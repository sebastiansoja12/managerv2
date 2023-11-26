package com.warehouse.routetracker.domain.vo;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RouteResponse {

    UUID id;
    Long parcelId;
}
