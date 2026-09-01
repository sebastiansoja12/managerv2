package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record CreateOperatorCourierRequest(String supplierCode,
                                           @NotBlank String firstName,
                                           @NotBlank String lastName,
                                           @NotBlank String telephoneNumber,
                                           @NotBlank String departmentCode) {
}
