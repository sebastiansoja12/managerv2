package com.warehouse.routetracker.infrastructure.api.event;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class RouteLogBaseEvent {

    @NonNull
    private final LocalDateTime localDateTime;


    RouteLogBaseEvent() {
        this.localDateTime = LocalDateTime.now();
    }
}
