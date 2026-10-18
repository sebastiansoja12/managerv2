package com.warehouse.pallet.domain.exception;

public class PalletLockedException extends RuntimeException {

    public PalletLockedException(final String message) {
        super(message);
    }
}
