package com.warehouse.message.api;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.message.domain.model.Message;

public enum ShipmentStatusDto {
    CREATED,

    REROUTE,

    SENT,

    DELIVERY,

    RETURN,

    REDIRECT;

    public static ShipmentStatusDto from(final Message message) {
        final ShipmentStatus shipmentStatus = message.getShipmentStatus();
        return switch (shipmentStatus) {
            case CREATED -> CREATED;
            case REROUTE -> REROUTE;
            case SENT -> SENT;
            case DELIVERY -> DELIVERY;
            case RETURN -> RETURN;
            case REDIRECT -> REDIRECT;
            default -> null;
        };
    }
}
