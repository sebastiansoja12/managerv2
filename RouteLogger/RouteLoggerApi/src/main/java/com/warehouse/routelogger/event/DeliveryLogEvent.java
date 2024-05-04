package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.DeliveryRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeliveryLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final DeliveryRequestDto deliveryRequest;

    @Builder
    public DeliveryLogEvent(@NonNull DeliveryRequestDto deliveryRequest) {
        super();
        this.deliveryRequest = deliveryRequest;
    }

}
