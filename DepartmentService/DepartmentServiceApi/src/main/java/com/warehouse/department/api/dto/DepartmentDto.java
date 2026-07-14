package com.warehouse.department.api.dto;

public record DepartmentDto(String departmentCode, String city, String street, String zipCode,
                            String countryCode, CoordinatesDto coordinates) {
}
