package com.warehouse.routetracker.infrastructure.adapter.primary.api.event;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.RouteResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
@Getter
public class RouteResponseEvent extends RouteLogBaseEvent implements RouteLogEvent {

    @NonNull
    private final RouteResponseDto routeResponse;

    @Builder
    public RouteResponseEvent(@NonNull RouteResponseDto routeResponse) {
        super();
        this.routeResponse = routeResponse;
    }
}
