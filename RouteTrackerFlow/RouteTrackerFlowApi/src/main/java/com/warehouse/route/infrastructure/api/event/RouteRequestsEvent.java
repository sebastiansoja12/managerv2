package com.warehouse.route.infrastructure.api.event;

import com.warehouse.route.infrastructure.api.RouteLogEvent;
import com.warehouse.route.infrastructure.api.dto.RouteRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class RouteRequestsEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final List<RouteRequestDto> request;

    @Builder
    RouteRequestsEvent(@NonNull List<RouteRequestDto> request) {
        super();
        this.request = request;
    }
}
