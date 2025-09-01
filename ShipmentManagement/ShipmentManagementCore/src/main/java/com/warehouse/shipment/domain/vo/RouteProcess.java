package com.warehouse.shipment.domain.vo;

import org.springframework.http.ResponseEntity;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecord;


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
        final RouteProcess routeProcess;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            assert responseEntity.getBody() != null;
            routeProcess = RouteProcess.from(shipmentId, responseEntity.getBody().processId(),
                    responseEntity.getBody().faultDescription().value(), responseEntity.getBody().returnCode().value());
        } else {
            routeProcess = RouteProcess.from(null, null, responseEntity.getStatusCode().toString(),
                    responseEntity.getStatusCode().toString());
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
