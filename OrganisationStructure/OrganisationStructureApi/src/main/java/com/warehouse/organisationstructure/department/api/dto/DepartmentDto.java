package com.warehouse.organisationstructure.department.api.dto;

public record DepartmentDto(Long departmentId, String departmentCode, String city, String street, String zipCode,
                            String countryCode, CoordinatesDto coordinates, String status) {

    public DepartmentDto(final Long departmentId, final String departmentCode, final String city, final String street,
                         final String zipCode, final String countryCode, final CoordinatesDto coordinates) {
        this(departmentId, departmentCode, city, street, zipCode, countryCode, coordinates, null);
    }
}
