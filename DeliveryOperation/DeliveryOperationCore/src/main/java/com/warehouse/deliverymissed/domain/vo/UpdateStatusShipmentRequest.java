package com.warehouse.deliverymissed.domain.vo;


import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

import lombok.Builder;

@Builder
public class UpdateStatusShipmentRequest {
    private final ShipmentId shipmentId;
    private final ShipmentStatus shipmentStatus = ShipmentStatus.DELIVERY;

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }
}
