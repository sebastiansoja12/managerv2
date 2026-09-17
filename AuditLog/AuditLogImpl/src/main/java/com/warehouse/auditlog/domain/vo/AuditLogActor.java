package com.warehouse.auditlog.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;

import java.util.Objects;

public record AuditLogActor(OperatorId operatorId,
                            DepartmentId departmentId,
                            UserId userId,
                            Username username) {

    public AuditLogActor {
        Objects.requireNonNull(operatorId, "AuditLog operator ID cannot be null");
    }

    public boolean system() {
        return this.userId == null;
    }
}
