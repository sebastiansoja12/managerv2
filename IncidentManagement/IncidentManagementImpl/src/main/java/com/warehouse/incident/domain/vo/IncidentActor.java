package com.warehouse.incident.domain.vo;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;

import java.util.Objects;

public record IncidentActor(UserId userId, Username username) {

    public IncidentActor {
        Objects.requireNonNull(userId, "Incident actor user ID cannot be null");
    }
}
