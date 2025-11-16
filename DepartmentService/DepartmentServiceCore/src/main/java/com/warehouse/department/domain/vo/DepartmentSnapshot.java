package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;

import java.time.Instant;

public record DepartmentSnapshot(
        DepartmentCode departmentCode,
        Address address,
        TaxId taxId,
        String telephoneNumber,
        String openingHours,
        String email,
        Boolean active,
        DepartmentType departmentType,
        Department.Status status,
        Instant createdAt,
        Instant updatedAt,
        UserId adminUserId,
        UserId createdBy,
        UserId lastModifiedBy
) {
}
