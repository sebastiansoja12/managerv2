package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class ReturnTrackRequest {
    private ShipmentId shipmentId;
    private ProcessType processType;
    private String username;
    private String depotCode;

    public ReturnTrackRequest() {
    }

    public ReturnTrackRequest(final ShipmentId shipmentId, final ProcessType processType, final String username,
                              final String depotCode) {
		this.shipmentId = shipmentId;
		this.processType = processType;
		this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(final String depotCode) {
        this.depotCode = depotCode;
    }
}
