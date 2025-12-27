package com.warehouse.department.api.dto;

public record DepartmentDto(String departmentCode, String city, String street, String country,
                            String zipCode, CoordinatesDto coordinates) {
}
