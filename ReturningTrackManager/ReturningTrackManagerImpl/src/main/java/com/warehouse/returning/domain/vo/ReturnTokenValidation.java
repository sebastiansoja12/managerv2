package com.warehouse.returning.domain.vo;

public record ReturnTokenValidation(ShipmentId shipmentId, boolean valid, String message) {

    public static ReturnTokenValidation valid(final ShipmentId shipmentId) {
        return new ReturnTokenValidation(shipmentId, true, "Return token is valid");
    }

    public static ReturnTokenValidation invalid(final ShipmentId shipmentId) {
        return new ReturnTokenValidation(shipmentId, false, "Wrong return token");
    }
}
