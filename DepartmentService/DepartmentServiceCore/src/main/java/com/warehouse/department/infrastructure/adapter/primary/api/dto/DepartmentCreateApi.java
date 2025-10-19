package com.warehouse.department.infrastructure.adapter.primary.api.dto;

public record DepartmentCreateApi(DepartmentCodeApi departmentCode,
                                  String city,
                                  String street,
                                  String country,
                                  String postalCode,
                                  String nip,
                                  String telephoneNumber,
                                  String openingHours,
                                  String email,
                                  String countryCode,
                                  String departmentType) {
}
