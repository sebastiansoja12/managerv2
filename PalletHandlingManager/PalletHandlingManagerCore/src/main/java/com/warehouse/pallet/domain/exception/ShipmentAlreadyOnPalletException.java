package com.warehouse.pallet.domain.exception;

public class ShipmentAlreadyOnPalletException extends RuntimeException {

    public ShipmentAlreadyOnPalletException(final String message) {
        super(message);
    }
}
