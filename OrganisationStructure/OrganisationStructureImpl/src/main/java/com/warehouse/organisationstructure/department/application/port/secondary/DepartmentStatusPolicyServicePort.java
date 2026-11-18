package com.warehouse.organisationstructure.department.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusPolicy;

public interface DepartmentStatusPolicyServicePort {
    DepartmentStatusPolicy policyFor(final DepartmentId departmentId);
}
