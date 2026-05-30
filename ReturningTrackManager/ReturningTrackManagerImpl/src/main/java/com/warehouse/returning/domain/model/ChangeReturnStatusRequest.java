package com.warehouse.returning.domain.model;

import com.warehouse.returning.domain.vo.ShipmentId;

public class ChangeReturnStatusRequest {
    private ShipmentId shipmentId;
    private ReturnStatus returnStatus;

    public ChangeReturnStatusRequest(final ShipmentId shipmentId,
                                     final ReturnStatus returnStatus) {
        this.shipmentId = shipmentId;
        this.returnStatus = returnStatus;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setReturnStatus(final ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }
}
