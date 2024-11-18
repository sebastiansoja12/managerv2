package com.warehouse.routelogger.event;

import com.warehouse.routelogger.dto.DepotCodeRequestDto;
import com.warehouse.routelogger.RouteLogEvent;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DepartmentCodeLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final DepotCodeRequestDto depotCodeRequest;

    @Builder
    public DepartmentCodeLogEvent(@NonNull DepotCodeRequestDto depotCodeRequest) {
        super();
        this.depotCodeRequest = depotCodeRequest;
    }

}
