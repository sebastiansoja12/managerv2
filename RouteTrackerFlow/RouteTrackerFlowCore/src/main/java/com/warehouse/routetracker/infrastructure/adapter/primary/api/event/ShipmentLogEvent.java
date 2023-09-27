package com.warehouse.routetracker.infrastructure.adapter.primary.api.event;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.ShipmentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ShipmentLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final ShipmentRequestDto shipmentRequest;

    @Builder
    ShipmentLogEvent(@NonNull ShipmentRequestDto shipmentRequest) {
        super();
        this.shipmentRequest = shipmentRequest;
    }
}
