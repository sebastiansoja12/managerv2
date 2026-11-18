package com.warehouse.fleettracking.domain.vo;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;

import java.util.Objects;

public record TrackingDriver(UserId userId, Username username) {

    public TrackingDriver {
        Objects.requireNonNull(userId, "Tracking driver user ID cannot be null");
    }
}
