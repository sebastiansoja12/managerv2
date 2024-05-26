package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.UsernameLogRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UsernameLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final UsernameLogRequestDto usernameLogRequest;

    @Builder
    public UsernameLogEvent(@NonNull UsernameLogRequestDto usernameLogRequest) {
        super();
        this.usernameLogRequest = usernameLogRequest;
    }
}
