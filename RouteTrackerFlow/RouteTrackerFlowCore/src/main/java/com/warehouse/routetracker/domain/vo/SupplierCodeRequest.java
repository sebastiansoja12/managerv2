package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class SupplierCodeRequest {
    private String supplierCode;
    private ShipmentId shipmentId;
    private ProcessType processType;

    public SupplierCodeRequest(final String supplierCode, final ShipmentId shipmentId, final ProcessType processType) {
        this.supplierCode = supplierCode;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
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
