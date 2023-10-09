package com.warehouse.routetracker.infrastructure.api.event;

import com.warehouse.routetracker.infrastructure.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.api.dto.ParcelStatusRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ParcelStatusEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final ParcelStatusRequestDto request;

    @Builder
    ParcelStatusEvent(@NonNull ParcelStatusRequestDto request) {
        super();
        this.request = request;
    }
}
