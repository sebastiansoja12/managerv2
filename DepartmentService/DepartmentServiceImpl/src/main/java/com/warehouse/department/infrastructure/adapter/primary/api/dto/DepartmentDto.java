package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import lombok.Value;

@Value
public class DepartmentDto {

    DepartmentCodeApi departmentCode;

    String city;

    String street;

    String country;

    String postalCode;

    String nip;

    String telephoneNumber;

    String openingHours;

    Boolean active;

    String countryCode;
}
