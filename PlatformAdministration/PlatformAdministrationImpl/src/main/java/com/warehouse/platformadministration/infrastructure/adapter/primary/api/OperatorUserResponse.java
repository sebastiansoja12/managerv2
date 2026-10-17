package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

public record OperatorUserResponse(ValueResponse<Long> userId,
                                   String username,
                                   String firstName,
                                   String lastName,
                                   String email,
                                   String role,
                                   String departmentCode,
                                   boolean deleted) {
}
