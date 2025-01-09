package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import lombok.Value;

@Value
public class DepartmentDto {

    DepartmentCodeDto departmentCode;

    String city;

    String street;

    String country;

    String postalCode;

    String nip;

    String telephoneNumber;

    String openingHours;

    Boolean active;
}
