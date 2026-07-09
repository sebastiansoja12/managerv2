package com.warehouse.routetracker.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;

public class FaultDescription {
    private ProcessType processType;
    private ShipmentId shipmentId;
    private String description;

	public FaultDescription(final ProcessType processType, final ShipmentId shipmentId, final String description) {
		this.processType = processType;
		this.shipmentId = shipmentId;
		this.description = description;
	}

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
