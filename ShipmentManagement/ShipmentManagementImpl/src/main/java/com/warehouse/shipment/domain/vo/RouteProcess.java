package com.warehouse.shipment.domain.vo;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatusCode;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;


public class RouteProcess {
    private final ShipmentId shipmentId;
    private final ProcessId processId;
    private final String faultDescription;
    private final String returnCode;

    public RouteProcess(final ShipmentId shipmentId, final ProcessId processId, 
                        final String faultDescription, 
                        final String returnCode) {
        this.shipmentId = shipmentId;
        this.processId = processId;
        this.faultDescription = faultDescription;
        this.returnCode = returnCode;
    }

    public static RouteProcess from(final ResponseEntity<RouteLogRecord> responseEntity, final ShipmentId shipmentId) {
        return from(responseEntity.getBody(), responseEntity.getStatusCode(), shipmentId);
    }

    public static RouteProcess from(final RouteLogRecord routeLogRecord,
                                    final HttpStatusCode statusCode,
                                    final ShipmentId shipmentId) {
        final RouteProcess routeProcess;
        if (statusCode.is2xxSuccessful()) {
            assert routeLogRecord != null;
            routeProcess = RouteProcess.from(shipmentId, routeLogRecord.processId(),
                    routeLogRecord.faultDescription().value(), routeLogRecord.returnCode().value());
        } else {
            routeProcess = RouteProcess.from(null, null, statusCode.toString(), statusCode.toString());
        }
        return routeProcess;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public static RouteProcess from(final ShipmentId shipmentId, final ProcessId processId,
                                    final String faultDescription, final String returnCode) {
        return new RouteProcess(shipmentId, processId, faultDescription, returnCode);
    }
}
