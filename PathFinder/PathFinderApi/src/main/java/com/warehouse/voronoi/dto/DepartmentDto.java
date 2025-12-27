package com.warehouse.voronoi.dto;

public record DepartmentDto(String departmentCode, String city, String street, String zipCode,
                            String country,
                            CoordinatesDto coordinates) {
}
