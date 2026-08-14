package com.warehouse.organisationstructure.api.dto;

public record FirstDepartmentDto(
        String departmentCode,
        String city,
        String street,
        String postalCode,
        String countryCode,
        String openingHours,
        String departmentType
) {
}
