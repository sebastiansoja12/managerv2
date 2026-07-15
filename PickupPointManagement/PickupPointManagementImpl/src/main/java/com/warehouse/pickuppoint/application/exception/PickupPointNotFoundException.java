package com.warehouse.pickuppoint.application.exception;

import com.warehouse.commonassets.identificator.PickupPointId;

public class PickupPointNotFoundException extends RuntimeException {

    public PickupPointNotFoundException(final PickupPointId pickupPointId) {
        super("Pickup point not found: " + pickupPointId.value());
    }
}
