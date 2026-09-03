package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

public record OperatorCourierResponse(Long supplierId,
                                      ValueResponse<String> supplierCode,
                                      String firstName,
                                      String lastName,
                                      String telephoneNumber,
                                      ValueResponse<String> departmentCode,
                                      String status) {
}
