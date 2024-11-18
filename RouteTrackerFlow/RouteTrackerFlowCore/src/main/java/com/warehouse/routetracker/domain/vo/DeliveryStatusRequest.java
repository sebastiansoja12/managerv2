package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class DeliveryStatusRequest {
    private ShipmentId shipmentId;
    private String supplierCode;
    private String depotCode;
    private ProcessType processType;

	public DeliveryStatusRequest(final ShipmentId shipmentId, final String supplierCode, final String depotCode,
			final ProcessType processType) {
		this.shipmentId = shipmentId;
		this.supplierCode = supplierCode;
		this.depotCode = depotCode;
		this.processType = processType;
	}

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(final String depotCode) {
        this.depotCode = depotCode;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }
}
