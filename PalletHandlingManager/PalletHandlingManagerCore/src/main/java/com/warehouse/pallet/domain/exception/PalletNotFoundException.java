package com.warehouse.pallet.domain.exception;

public class PalletNotFoundException extends RuntimeException {

    public PalletNotFoundException(final String message) {
        super(message);
    }
}
