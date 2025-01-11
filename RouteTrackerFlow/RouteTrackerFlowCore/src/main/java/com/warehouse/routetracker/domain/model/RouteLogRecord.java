package com.warehouse.routetracker.domain.model;


import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.Error;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteLogRecord {
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

    public void saveErrorReturnCode(final Error error) {
        this.returnCode = error.getValue();
    }

    public void saveFaultDescription(final String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public void saveTerminalId(final ProcessType processType, final TerminalId terminalId) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveTerminalId(terminalId);
    }

    public void saveDeviceVersion(final ProcessType processType, final String version) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveZebraVersionInformation(version);
    }

    public void saveUsername(final ProcessType processType, final String username) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveUsername(username);
    }

    public void saveDepotCode(final ProcessType processType, final String depotCode) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveDepartmentCode(depotCode);
    }

    public void saveDescription(final ProcessType processType, final String description) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveDescription(description);
    }

    public void saveSupplierCode(final ProcessType processType, final String supplierCode) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveSupplierCode(supplierCode);
    }

    public void updateShipmentStatus(final ProcessType processType, final ParcelStatus parcelStatus) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveParcelStatus(parcelStatus);
    }

    public void updateRequest(final ProcessType processType, final String request) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);

        routeLogRecordDetail.updateRequest(request);
    }

    public void updateDeviceInformation(final DeviceInformationRequest request) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(request.getProcessType());
        routeLogRecordDetail.updateDeviceInformation(request);
    }
}
