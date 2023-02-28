package com.warehouse.route.infrastructure.api.event;

import com.warehouse.route.infrastructure.api.RouteLogEvent;
import com.warehouse.route.infrastructure.api.dto.RouteResponseDto;
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
