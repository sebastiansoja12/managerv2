package com.warehouse.routetracker.domain.model;


import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.Error;
import com.warehouse.routetracker.domain.vo.TerminalId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteLogRecord {
    private UUID id;
    private ShipmentId shipmentId;
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

    public void createShipmentEvent(final String eventType,
                                    final ShipmentStatus shipmentStatus,
                                    final LocalDateTime occurredAt,
                                    final String payload,
                                    final UserId userId,
                                    final DepartmentId departmentId) {
        getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet().add(RouteLogRecordDetail.builder()
                .shipmentStatus(shipmentStatus)
                .processType(determineProcessType(shipmentStatus))
                .description(eventType)
                .timestamp(occurredAt)
                .request(payload)
                .userId(userId)
                .departmentId(departmentId)
                .build());
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

    public void saveUserId(final ProcessType processType, final UserId userId) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveUserId(userId);
    }

    public void saveDepartmentId(final ProcessType processType, final DepartmentId departmentId) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveDepartmentId(departmentId);
    }

    public void saveDescription(final ProcessType processType, final String description) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveDescription(description);
    }

    public void saveSupplierId(final ProcessType processType, final SupplierId supplierId) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveSupplierId(supplierId);
    }

    public void updateShipmentStatus(final ProcessType processType, final ShipmentStatus shipmentStatus) {
        final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetail(processType);
        routeLogRecordDetail.saveShipmentStatus(shipmentStatus);
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

    public void changePerson(final Person person) {
        final String description = "%s changed to %s, %s";
		final RouteLogRecordDetail routeLogRecordDetail = getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet().stream()
                .findFirst()
                .orElseThrow();
        getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet()
                .add(RouteLogRecordDetail.builder()
						.description(String.format(description, person.getPersonType(), person.getFirstName(),
								person.getLastName()))
                        .processType(routeLogRecordDetail.getProcessType())
                        .shipmentStatus(routeLogRecordDetail.getShipmentStatus())
                        .build());
        
    }

    private ProcessType determineProcessType(final ShipmentStatus shipmentStatus) {
        return switch (shipmentStatus) {
            case CREATED, PREPARED, ACCEPTED -> ProcessType.CREATED;
            case REROUTE -> ProcessType.REROUTE;
            case SENT, DELIVERY -> ProcessType.ROUTE;
            case RETURN -> ProcessType.RETURN;
            case REDIRECT -> ProcessType.REDIRECT;
            case CANCELED -> ProcessType.CANCELED;
        };
    }
}
