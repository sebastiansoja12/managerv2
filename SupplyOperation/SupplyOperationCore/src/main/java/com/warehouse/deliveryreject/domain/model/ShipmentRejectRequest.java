package com.warehouse.deliveryreject.domain.model;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public class ShipmentRejectRequest {
    private ShipmentId shipmentId;
    private ProcessType processType;
    private DeliveryStatus deliveryStatus;
    private ShipmentStatus shipmentStatus;

    public ShipmentRejectRequest(final ShipmentId shipmentId,
                                 final ProcessType processType,
                                 final DeliveryStatus deliveryStatus,
                                 final ShipmentStatus shipmentStatus) {
        this.shipmentId = shipmentId;
        this.processType = processType;
        this.deliveryStatus = deliveryStatus;
        this.shipmentStatus = shipmentStatus;
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

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
}
