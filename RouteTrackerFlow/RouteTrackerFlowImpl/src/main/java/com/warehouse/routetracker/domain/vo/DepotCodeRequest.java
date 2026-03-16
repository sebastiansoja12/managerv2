package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class DepotCodeRequest {
    private String depotCode;
    private ShipmentId shipmentId;
    private ProcessType processType;

    public DepotCodeRequest(final String depotCode, final ShipmentId shipmentId, final ProcessType processType) {
        this.depotCode = depotCode;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(final String depotCode) {
        this.depotCode = depotCode;
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
