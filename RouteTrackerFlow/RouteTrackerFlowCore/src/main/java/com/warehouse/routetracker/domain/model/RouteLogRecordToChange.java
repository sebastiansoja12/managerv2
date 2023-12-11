package com.warehouse.routetracker.domain.model;

// TODO it will be RouteLogRecord

import com.warehouse.routetracker.domain.vo.Error;
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

    public void saveTerminalRequest(ProcessType processType, String terminalRequest) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);

        routeLogRecordDetail.updateRequest(terminalRequest);
    }

    public void saveReturnTrackRequest(ProcessType processType, String request) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);

        routeLogRecordDetail.updateRequest(request);
    }

    public void saveZebraIdInformation(ProcessType processType, Long zebraId) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveZebraIdInformation(zebraId);
    }

    public void saveZebraVersionInformation(ProcessType processType, String version) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveZebraVersionInformation(version);
    }

    public void saveUsername(ProcessType processType, String username) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveUsername(username);
    }

    public void saveDepotCode(ProcessType processType, String depotCode) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveDepotCode(depotCode);
    }

    public void saveDescription(ProcessType processType, String description) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveDescription(description);
    }

    public void saveSupplierCode(ProcessType processType, String supplierCode) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveSupplierCode(supplierCode);
    }
}
