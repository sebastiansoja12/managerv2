package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ReturnTokenValidationResult(ShipmentId shipmentId, boolean valid, String message) {

    public static ReturnTokenValidationResult valid(final ShipmentId shipmentId) {
        return new ReturnTokenValidationResult(shipmentId, true, "Return token is valid");
    }

    public static ReturnTokenValidationResult invalid(final ShipmentId shipmentId) {
        return new ReturnTokenValidationResult(shipmentId, false, "Wrong return token");
    }
}
