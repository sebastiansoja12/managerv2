package com.warehouse.pallet.infrastructure.adapter.primary;

import com.warehouse.pallet.domain.exception.MaxPalletWeightAlreadySetException;
import com.warehouse.pallet.domain.exception.PalletLockedException;
import com.warehouse.pallet.domain.exception.PalletModificationException;
import com.warehouse.pallet.domain.exception.PalletNotFoundException;
import com.warehouse.pallet.domain.exception.PalletStatusTransitionException;
import com.warehouse.pallet.domain.exception.ShipmentAlreadyOnPalletException;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletErrorApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = PalletStorageController.class)
public class PalletExceptionHandler {

    @ExceptionHandler(PalletNotFoundException.class)
    public ResponseEntity<PalletErrorApi> handle(final PalletNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, "PALLET_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(PalletLockedException.class)
    public ResponseEntity<PalletErrorApi> handle(final PalletLockedException exception) {
        return error(HttpStatus.CONFLICT, "PALLET_LOCKED", exception.getMessage());
    }

    @ExceptionHandler(MaxPalletWeightAlreadySetException.class)
    public ResponseEntity<PalletErrorApi> handle(final MaxPalletWeightAlreadySetException exception) {
        return error(HttpStatus.CONFLICT, "PALLET_MAX_WEIGHT_ALREADY_SET", exception.getMessage());
    }

    @ExceptionHandler(ShipmentAlreadyOnPalletException.class)
    public ResponseEntity<PalletErrorApi> handle(final ShipmentAlreadyOnPalletException exception) {
        return error(HttpStatus.CONFLICT, "SHIPMENT_ALREADY_ON_PALLET", exception.getMessage());
    }

    @ExceptionHandler({PalletModificationException.class, PalletStatusTransitionException.class})
    public ResponseEntity<PalletErrorApi> handle(final RuntimeException exception) {
        return error(HttpStatus.CONFLICT, "PALLET_INVALID_STATE", exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, MissingRequestHeaderException.class})
    public ResponseEntity<PalletErrorApi> handleBadRequest(final Exception exception) {
        return error(HttpStatus.BAD_REQUEST, "PALLET_BAD_REQUEST", exception.getMessage());
    }

    private ResponseEntity<PalletErrorApi> error(final HttpStatus status, final String code, final String message) {
        return ResponseEntity.status(status).body(new PalletErrorApi(code, message));
    }
}
