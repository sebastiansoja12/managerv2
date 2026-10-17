package com.warehouse.organisationstructure.department.api.dto;

public record CreateDepartmentApiRequest(
        DepartmentCodeDto departmentCode,
        String city,
        String street,
        String postalCode,
        String countryCode,
        String taxId,
        String telephoneNumber,
        String openingHours,
        String email,
        DepartmentTypeDto departmentType) {
}
