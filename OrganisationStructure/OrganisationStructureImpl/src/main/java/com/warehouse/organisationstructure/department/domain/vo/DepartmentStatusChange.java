package com.warehouse.organisationstructure.department.domain.vo;

import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public record DepartmentStatusChange(UserId modifiedBy,
                                     String reason,
                                     DepartmentStatusPolicy policy,
                                     List<String> activationBlockers,
                                     List<String> activeWork) {

    public DepartmentStatusChange {
        policy = policy == null ? DepartmentStatusPolicy.unrestricted() : policy;
        activationBlockers = activationBlockers == null ? List.of() : List.copyOf(activationBlockers);
        activeWork = activeWork == null ? List.of() : List.copyOf(activeWork);
        reason = reason == null || reason.isBlank() ? null : reason.trim();
    }
}
