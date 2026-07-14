package com.warehouse.organisationstructure.api.dto;

import java.time.LocalDate;

public record CreateOperatorApiRequest(String userFirstName,
                                       String userLastName,
                                       String username,
                                       String password,
                                       String language,
                                       String email,
                                       String taxId,
                                       boolean supportsLockers,
                                       boolean supportsInternationalShipping,
                                       boolean supportsCashOnDelivery,
                                       String contactPhone,
                                       String contactEmail,
                                       String companyName,
                                       LocalDate contractStartDate,
                                       LocalDate contractEndDate,
                                       LocalDate foundedDate,
                                       OperatorConfigurationDto configuration,
                                       FirstDepartmentDto firstDepartment) {

    public record FirstDepartmentDto(String departmentCode,
                                     String city,
                                     String street,
                                     String postalCode,
                                     String countryCode,
                                     String openingHours,
                                     String departmentType) {
    }
}
