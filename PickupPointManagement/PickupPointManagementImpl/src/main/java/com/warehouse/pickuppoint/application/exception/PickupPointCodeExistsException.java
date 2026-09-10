package com.warehouse.pickuppoint.application.exception;

import com.warehouse.pickuppoint.domain.vo.PickupPointCode;

public class PickupPointCodeExistsException extends RuntimeException {

    public PickupPointCodeExistsException(final PickupPointCode code) {
        super("Pickup point code already exists: " + code.value());
    }
}
