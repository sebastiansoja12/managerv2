package com.warehouse.route.infrastructure.api.event;

import com.warehouse.route.infrastructure.api.RouteLogEvent;
import com.warehouse.route.infrastructure.api.dto.SupplyInformationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SupplyLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    @NonNull
    private final SupplyInformationDto supplyInformation;

    @Builder
    SupplyLogEvent(@NonNull SupplyInformationDto supplyInformation) {
        super();
        this.supplyInformation = supplyInformation;
    }
}
