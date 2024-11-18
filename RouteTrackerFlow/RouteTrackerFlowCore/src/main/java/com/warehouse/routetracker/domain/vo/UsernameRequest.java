package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class UsernameRequest {
    private String username;
    private ShipmentId shipmentId;
    private ProcessType processType;

    public UsernameRequest(final String username, final ShipmentId shipmentId, final ProcessType processType) {
        this.username = username;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }
}
