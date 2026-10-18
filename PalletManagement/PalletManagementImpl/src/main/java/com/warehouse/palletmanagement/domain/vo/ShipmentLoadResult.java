package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentLoadResult(ShipmentId shipmentId, boolean loaded, String shipmentStatus, String errorMessage) {

    public static ShipmentLoadResult loaded(final ShipmentId shipmentId, final String shipmentStatus) {
        return new ShipmentLoadResult(shipmentId, true, shipmentStatus, null);
    }

    public static ShipmentLoadResult failed(final ShipmentId shipmentId, final String errorMessage) {
        return new ShipmentLoadResult(shipmentId, false, null, errorMessage);
    }
}
