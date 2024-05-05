package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.VersionLogRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class VersionLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final VersionLogRequestDto versionLogRequest;

    @Builder
    public VersionLogEvent(@NonNull VersionLogRequestDto versionLogRequest) {
        super();
        this.versionLogRequest = versionLogRequest;
    }
}
