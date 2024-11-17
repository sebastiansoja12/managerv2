package com.warehouse.delivery.domain.model;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;


public class DeliveryResponse {
    private DeliveryId deliveryId;
    private UpdateStatus updateStatus;
    private ShipmentId shipmentId;

    public DeliveryResponse(final DeliveryId deliveryId, final UpdateStatus updateStatus, final ShipmentId shipmentId) {
        this.deliveryId = deliveryId;
        this.updateStatus = updateStatus;
        this.shipmentId = shipmentId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(final DeliveryId deliveryId) {
        this.deliveryId = deliveryId;
    }

    public UpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(final UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }
}
