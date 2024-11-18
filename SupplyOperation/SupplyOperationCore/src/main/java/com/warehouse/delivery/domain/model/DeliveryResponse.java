package com.warehouse.delivery.domain.model;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.enumeration.DeliverySaveStatus;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;


public class DeliveryResponse {
    private DeliveryId deliveryId;
    private UpdateStatus updateStatus;
    private ShipmentId shipmentId;
    private DeliverySaveStatus deliverySaveStatus;

    public DeliveryResponse(final DeliveryId deliveryId,
                            final UpdateStatus updateStatus,
                            final ShipmentId shipmentId,
                            final DeliverySaveStatus deliverySaveStatus) {
        this.deliveryId = deliveryId;
        this.updateStatus = updateStatus;
        this.shipmentId = shipmentId;
        this.deliverySaveStatus = deliverySaveStatus;
    }

    public DeliverySaveStatus getDeliverySaveStatus() {
        return deliverySaveStatus;
    }

    public void setDeliverySaveStatus(final DeliverySaveStatus deliverySaveStatus) {
        this.deliverySaveStatus = deliverySaveStatus;
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
