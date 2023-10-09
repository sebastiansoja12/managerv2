package com.warehouse.routetracker.infrastructure.api.event;

import com.warehouse.routetracker.infrastructure.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.api.dto.ShipmentRequestDto;
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
