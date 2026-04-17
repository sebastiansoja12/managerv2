package com.warehouse.organisationstructure.operator.domain.vo;

public record OperatorProvisioningDetails(String userFirstName,
                                          String userLastName,
                                          String username,
                                          String password,
                                          String language,
                                          String email,
                                          FirstDepartment firstDepartment) {

    public record FirstDepartment(String departmentCode,
                                  String city,
                                  String street,
                                  String postalCode,
                                  String countryCode,
                                  String openingHours,
                                  String departmentType) {
    }
}
