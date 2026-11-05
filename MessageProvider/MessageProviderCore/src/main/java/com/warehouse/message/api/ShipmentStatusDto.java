package com.warehouse.message.api;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.message.domain.model.Message;

public enum ShipmentStatusDto {
    PLANNED,

    CREATED,

    PREPARED,

    COLLECTED,

    ACCEPTED,

    REROUTE,

    SENT,

    DELIVERY,

    RETURN,

    REDIRECT;

    public static ShipmentStatusDto from(final Message message) {
        final ShipmentStatus shipmentStatus = message.getShipmentStatus();
        return switch (shipmentStatus) {
            case PLANNED -> PLANNED;
            case CREATED -> CREATED;
            case PREPARED -> PREPARED;
            case COLLECTED -> COLLECTED;
            case ACCEPTED -> ACCEPTED;
            case REROUTE -> REROUTE;
            case SENT -> SENT;
            case DELIVERY -> DELIVERY;
            case RETURN -> RETURN;
            case REDIRECT -> REDIRECT;
            default -> null;
        };
    }
}
