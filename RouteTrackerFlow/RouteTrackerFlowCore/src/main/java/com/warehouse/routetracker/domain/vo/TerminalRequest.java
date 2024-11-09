package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class TerminalRequest {
    private ProcessType processType;
    private ShipmentId shipmentId;
    private String requestAsJson;

    public TerminalRequest(final ProcessType processType, final ShipmentId shipmentId, final String requestAsJson) {
        this.processType = processType;
        this.shipmentId = shipmentId;
        this.requestAsJson = requestAsJson;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setRequestAsJson(final String requestAsJson) {
        this.requestAsJson = requestAsJson;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public String getRequestAsJson() {
        return requestAsJson;
    }
}
