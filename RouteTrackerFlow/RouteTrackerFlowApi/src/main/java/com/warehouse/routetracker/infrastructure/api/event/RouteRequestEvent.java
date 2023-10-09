package com.warehouse.routetracker.infrastructure.api.event;

import com.warehouse.routetracker.infrastructure.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RouteRequestEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final RouteRequestDto routeRequestDto;

    @Builder
    RouteRequestEvent(@NonNull RouteRequestDto routeRequestDto) {
        super();
        this.routeRequestDto = routeRequestDto;
    }
}
