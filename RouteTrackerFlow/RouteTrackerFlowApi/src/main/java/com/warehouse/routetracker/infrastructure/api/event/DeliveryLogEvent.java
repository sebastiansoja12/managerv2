package com.warehouse.routetracker.infrastructure.api.event;

import java.util.List;

import com.warehouse.routetracker.infrastructure.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeliveryLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    @NonNull
    private final List<DeliveryInformationDto> deliveryInformation;

    @Builder
    DeliveryLogEvent(@NonNull List<DeliveryInformationDto> deliveryInformation) {
        super();
        this.deliveryInformation = deliveryInformation;
    }
}
