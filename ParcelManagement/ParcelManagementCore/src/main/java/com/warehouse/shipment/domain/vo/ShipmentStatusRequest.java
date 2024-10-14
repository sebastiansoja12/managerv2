package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

@Builder
public class ShipmentStatusRequest {
    private final ShipmentId shipmentId;
    private final ShipmentStatus shipmentStatus;

    public ShipmentStatusRequest(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        this.shipmentId = shipmentId;
        this.shipmentStatus = shipmentStatus;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }
}
