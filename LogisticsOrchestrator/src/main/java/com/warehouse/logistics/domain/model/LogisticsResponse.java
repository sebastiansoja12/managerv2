package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.enumeration.DeliverySaveStatus;
import com.warehouse.logistics.infrastructure.adapter.secondary.api.UpdateStatus;


public class LogisticsResponse {
    private DeliveryId deliveryId;
    private UpdateStatus updateStatus;
    private ShipmentId shipmentId;
    private DeliverySaveStatus deliverySaveStatus;

    public LogisticsResponse(final DeliveryId deliveryId,
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
