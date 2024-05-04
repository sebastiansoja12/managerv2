package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.RequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RequestLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final RequestDto request;

    @Builder
    public RequestLogEvent(@NonNull RequestDto request) {
        super();
        this.request = request;
    }
}
