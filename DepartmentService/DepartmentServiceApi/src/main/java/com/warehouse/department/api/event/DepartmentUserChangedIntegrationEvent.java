package com.warehouse.department.api.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.dto.DepartmentCodeDto;

import java.time.Instant;
import java.util.Objects;

@IntegrationEventType(value = "department.user.changed", version = 1)
public record DepartmentUserChangedIntegrationEvent(
        DepartmentCodeDto departmentCode,
        UserId userId,
        String telephoneNumber,
        String email,
        Instant timestamp) implements IntegrationEvent, IntegrationEventKey {

    public DepartmentUserChangedIntegrationEvent {
        Objects.requireNonNull(departmentCode, "Department code cannot be null");
        Objects.requireNonNull(departmentCode.value(), "Department code value cannot be null");
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(userId.value(), "User ID value cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    @Override
    public String eventKey() {
        return this.departmentCode.value();
    }
}
