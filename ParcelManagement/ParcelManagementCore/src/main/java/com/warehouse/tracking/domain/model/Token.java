package com.warehouse.tracking.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ShipmentId;

public class Token {
    private String token;
    private ShipmentId shipmentId;
    private ProcessType processType;

    public Token(final String token, final ShipmentId shipmentId, final ProcessType processType) {
        this.token = token;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getToken() {
        return token;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ProcessType getProcessType() {
        return processType;
    }
}
