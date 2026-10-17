package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

public record OperatorApiKeyResponse(Long userId,
                                     String username,
                                     String firstName,
                                     String lastName,
                                     String role,
                                     String departmentCode) {
}
