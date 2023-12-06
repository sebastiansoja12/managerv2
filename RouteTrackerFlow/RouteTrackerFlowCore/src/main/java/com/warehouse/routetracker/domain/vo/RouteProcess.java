package com.warehouse.routetracker.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class RouteProcess {
    Long parcelId;
    UUID processId;
}
