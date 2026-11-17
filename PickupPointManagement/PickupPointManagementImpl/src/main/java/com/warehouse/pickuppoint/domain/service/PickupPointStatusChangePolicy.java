package com.warehouse.pickuppoint.domain.service;

import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointStatusTransitionException;

public final class PickupPointStatusChangePolicy {

    private PickupPointStatusChangePolicy() {
    }

    public static void ensureRequestable(final PickupPointStatus currentStatus, final PickupPointStatus targetStatus) {
        if (targetStatus == PickupPointStatus.DELETED) {
            throw new InvalidPickupPointStatusTransitionException(currentStatus, targetStatus);
        }
    }
}
