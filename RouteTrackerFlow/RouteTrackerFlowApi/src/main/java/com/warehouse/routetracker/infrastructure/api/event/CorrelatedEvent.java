package com.warehouse.routetracker.infrastructure.api.event;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class CorrelatedEvent extends RouteLogBaseEvent {

    private final String correlationId;

    CorrelatedEvent(@NonNull String correlationId) {
        super();
        this.correlationId = correlationId;
    }
}
