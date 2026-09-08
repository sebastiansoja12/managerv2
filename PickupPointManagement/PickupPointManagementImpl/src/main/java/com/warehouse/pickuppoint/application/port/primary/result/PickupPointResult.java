package com.warehouse.pickuppoint.application.port.primary.result;

import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;
import java.util.Objects;

public record PickupPointResult(
        PickupPointSnapshot pickupPoint,
        PickupPointDepartment department,
        Boolean openNow,
        Instant evaluatedAt) {

    public PickupPointResult {
        Objects.requireNonNull(pickupPoint, "Pickup point snapshot cannot be null");
        Objects.requireNonNull(evaluatedAt, "Evaluation timestamp cannot be null");
    }
}
