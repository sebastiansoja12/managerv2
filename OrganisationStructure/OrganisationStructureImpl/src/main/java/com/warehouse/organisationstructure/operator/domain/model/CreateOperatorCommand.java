package com.warehouse.organisationstructure.operator.domain.model;

import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.time.LocalDate;

public record CreateOperatorCommand(
        String userFirstName,
        String userLastName,
        String username,
        String password,
        String language,
        String email,
        TaxId taxId,
        boolean supportsLockers,
        boolean supportsInternationalShipping,
        boolean supportsCashOnDelivery,
        String contactPhone,
        String contactEmail,
        String companyName,
        LocalDate contractStartDate,
        LocalDate contractEndDate,
        LocalDate foundedDate,
        OperatorConfiguration configuration,
        FirstDepartment firstDepartment
) {

    public record FirstDepartment(String departmentCode,
                                  String city,
                                  String street,
                                  String postalCode,
                                  String countryCode,
                                  String openingHours,
                                  String departmentType) {
    }
}
