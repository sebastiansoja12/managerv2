package com.warehouse.pallet.domain.exception;

public class PalletStatusTransitionException extends RuntimeException {

    public PalletStatusTransitionException(final String message) {
        super(message);
    }
}
