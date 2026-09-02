package com.warehouse.department.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.department.api.dto.DepartmentCodeDto;
import com.warehouse.department.api.dto.DepartmentIdDto;
import com.warehouse.department.api.dto.DepartmentStatusDto;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "department.status.changed", version = 1)
public final class DepartmentStatusChangedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final DepartmentIdDto affectedDepartmentId;
    private final DepartmentCodeDto departmentCode;
    private final DepartmentStatusDto status;

    @JsonCreator
    public DepartmentStatusChangedIntegrationEvent(
            @JsonProperty("affectedDepartmentId") final DepartmentIdDto affectedDepartmentId,
            @JsonProperty("departmentCode") final DepartmentCodeDto departmentCode,
            @JsonProperty("status") final DepartmentStatusDto status) {
        this.affectedDepartmentId = Objects.requireNonNull(
                affectedDepartmentId, "Affected department ID cannot be null");
        Objects.requireNonNull(this.affectedDepartmentId.value(), "Affected department ID value cannot be null");
        this.departmentCode = Objects.requireNonNull(departmentCode, "Department code cannot be null");
        Objects.requireNonNull(this.departmentCode.value(), "Department code value cannot be null");
        this.status = Objects.requireNonNull(status, "Department status cannot be null");
    }

    @JsonProperty("affectedDepartmentId")
    public DepartmentIdDto affectedDepartmentId() {
        return this.affectedDepartmentId;
    }

    @JsonProperty("departmentCode")
    public DepartmentCodeDto departmentCode() {
        return this.departmentCode;
    }

    @JsonProperty("status")
    public DepartmentStatusDto status() {
        return this.status;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.affectedDepartmentId.value());
    }
}
