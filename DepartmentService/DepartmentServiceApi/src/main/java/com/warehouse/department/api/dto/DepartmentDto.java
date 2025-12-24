package com.warehouse.department.api.dto;

public record DepartmentDto(String departmentCode, String city, String street, String country, CoordinatesDto coordinates) {
}
