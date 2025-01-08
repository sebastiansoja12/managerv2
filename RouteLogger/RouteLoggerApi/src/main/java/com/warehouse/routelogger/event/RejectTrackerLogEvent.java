package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.RejectTrackerRequestDto;
import lombok.Builder;

public class RejectTrackerLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final RejectTrackerRequestDto rejectTrackerRequest;

    @Builder
    public RejectTrackerLogEvent(final RejectTrackerRequestDto rejectTrackerRequest) {
        super();
        this.rejectTrackerRequest = rejectTrackerRequest;
    }

    public RejectTrackerRequestDto getRejectTrackerRequest() {
        return rejectTrackerRequest;
    }
}
