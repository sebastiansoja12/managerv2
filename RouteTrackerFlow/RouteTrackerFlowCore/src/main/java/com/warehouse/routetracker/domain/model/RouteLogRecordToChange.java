package com.warehouse.routetracker.domain.model;

// TODO it will be RouteLogRecord

import com.warehouse.routetracker.domain.vo.ReturnTrackRequest;
import com.warehouse.routetracker.domain.vo.TerminalRequest;
import lombok.*;

import java.util.UUID;

@Builder
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteLogRecordToChange {
    private UUID id;
    private Long parcelId;
    private RouteLogRecordDetails routeLogRecordDetails;
    private String returnCode;
    private String faultDescription;

    public RouteLogRecordDetails getRouteLogRecordDetails() {
        if (routeLogRecordDetails == null) {
            routeLogRecordDetails = new RouteLogRecordDetails();
        }
        return routeLogRecordDetails;
    }

    public void saveErrorReturnCode(Error error) {
        this.returnCode = error.getValue();
    }

    public void saveFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public void saveTerminalRequest(TerminalRequest request) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(request.getProcessType(), request.getId());

        routeLogRecordDetail.updateRequest(request.getRequest(), request.getTimestamp());
    }

    void saveReturnTrackRequest(ReturnTrackRequest request) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(request.getProcessType(), request.getId());

        routeLogRecordDetail.updateRequest(request.getRequest(), request.getTimestamp());
    }


    public void update(RouteLogRecordDetail routeLogRecordDetail) {
        getRouteLogRecordDetails().getRouteLogRecordDetail(routeLogRecordDetail.getProcessType(), routeLogRecordDetail.getZebraId());
    }
}
