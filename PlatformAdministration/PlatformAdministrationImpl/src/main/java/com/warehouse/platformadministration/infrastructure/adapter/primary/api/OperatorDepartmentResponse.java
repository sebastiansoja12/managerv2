package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

public record OperatorDepartmentResponse(Long departmentId,
                                         ValueResponse<String> departmentCode,
                                         AddressResponse address,
                                         String taxId,
                                         String telephoneNumber,
                                         String openingHours,
                                         String email,
                                         String departmentType,
                                         String status) {
}
