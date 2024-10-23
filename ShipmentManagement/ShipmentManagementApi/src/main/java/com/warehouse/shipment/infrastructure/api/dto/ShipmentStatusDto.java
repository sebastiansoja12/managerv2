package com.warehouse.shipment.infrastructure.api.dto;

import com.warehouse.commonassets.enumeration.ShipmentStatus;

public enum ShipmentStatusDto {
    CREATED,

    REROUTE,

    SENT,

    DELIVERY,

    RETURN,

    REDIRECT;

    public static ShipmentStatusDto from(final ShipmentStatus shipmentStatus) {
        return ShipmentStatusDto.valueOf(shipmentStatus.name());
    }
}
