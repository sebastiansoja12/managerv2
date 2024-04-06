package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.DepotCodeRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DepotCodeLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private DepotCodeRequestDto depotCodeRequest;

    @Builder
    public DepotCodeLogEvent(@NonNull DepotCodeRequestDto depotCodeRequest) {
        super();
        this.depotCodeRequest = depotCodeRequest;
    }

}