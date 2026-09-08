package com.warehouse.pickuppoint.application.port.primary.result;

import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchItem;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.time.Instant;

public record PickupPointListItemResult(
        PickupPointSearchItem pickupPoint,
        PickupPointDepartment department,
        Instant evaluatedAt) {
}
