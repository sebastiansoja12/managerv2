package com.warehouse.department.domain.vo;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;

public record DepartmentSnapshot(
        DepartmentCode departmentCode,
        Address address,
        TaxId taxId,
        String telephoneNumber,
        String openingHours,
        String email,
        DepartmentType departmentType,
        Department.Status status,
        Instant createdAt,
        Instant updatedAt,
        UserId adminUserId,
        UserId createdBy,
        UserId lastModifiedBy
) {
}
