package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class DescriptionRequest {
    private String value;
    private ShipmentId shipmentId;
    private ProcessType processType;

    public DescriptionRequest(final String value, final ShipmentId shipmentId, final ProcessType processType) {
        this.value = value;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
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
