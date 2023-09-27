package com.warehouse.routetracker.infrastructure.adapter.primary.api.event;

import java.util.List;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.RouteLogEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.SupplyInformationDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SupplyLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    @NonNull
    private final List<SupplyInformationDto> supplyInformation;

    @Builder
    SupplyLogEvent(@NonNull List<SupplyInformationDto> supplyInformation) {
        super();
        this.supplyInformation = supplyInformation;
    }
}
