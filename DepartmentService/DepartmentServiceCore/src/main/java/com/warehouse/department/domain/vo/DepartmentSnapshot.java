package com.warehouse.department.domain.vo;

public record DepartmentSnapshot(
        DepartmentCode departmentCode,
        Address address,
        String nip,
        String telephoneNumber,
        String openingHours,
        Boolean active
) {
}
