package com.warehouse.pallet.domain.exception;

public class MaxPalletWeightAlreadySetException extends RuntimeException {

    public MaxPalletWeightAlreadySetException(final String message) {
        super(message);
    }
}
