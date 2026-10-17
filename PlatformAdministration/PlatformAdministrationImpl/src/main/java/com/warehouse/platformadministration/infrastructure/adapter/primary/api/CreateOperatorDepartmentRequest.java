package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateOperatorDepartmentRequest(@NotBlank String departmentCode,
                                              @NotBlank String city,
                                              @NotBlank String street,
                                              @NotBlank String postalCode,
                                              @NotBlank String countryCode,
                                              @NotBlank String taxId,
                                              @NotBlank String telephoneNumber,
                                              @NotBlank String openingHours,
                                              @NotBlank @Email String email,
                                              @NotBlank String departmentType) {
}
