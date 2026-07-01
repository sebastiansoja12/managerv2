package com.warehouse.pickuppoint.domain.exception;

import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;

public class InvalidPickupPointStatusTransitionException extends RuntimeException {

    public InvalidPickupPointStatusTransitionException(
            final PickupPointStatus currentStatus,
            final PickupPointStatus targetStatus) {
        super("Cannot change pickup point status from " + currentStatus + " to " + targetStatus);
    }
}
