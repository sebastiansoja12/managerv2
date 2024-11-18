package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.model.RejectReason;


public class DeliveryRejectResponse {
    private SupplierCode supplierCode;
    private ShipmentId shipmentId;
    private ShipmentId newShipmentId;
    private RejectReason rejectReason;

	public DeliveryRejectResponse(final SupplierCode supplierCode,
                                  final ShipmentId shipmentId,
                                  final ShipmentId newShipmentId,
                                  final RejectReason rejectReason) {
        this.supplierCode = supplierCode;
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.rejectReason = rejectReason;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentId getNewShipmentId() {
        return newShipmentId;
    }

    public void setNewShipmentId(final ShipmentId newShipmentId) {
        this.newShipmentId = newShipmentId;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }
}