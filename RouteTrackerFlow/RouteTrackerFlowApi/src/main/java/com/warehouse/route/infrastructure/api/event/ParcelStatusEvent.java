package com.warehouse.route.infrastructure.api.event;

import com.warehouse.route.infrastructure.api.RouteLogEvent;
import com.warehouse.route.infrastructure.api.dto.ParcelStatusRequestDto;
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
