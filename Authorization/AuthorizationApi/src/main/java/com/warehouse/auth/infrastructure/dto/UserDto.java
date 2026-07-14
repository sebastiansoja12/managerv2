package com.warehouse.auth.infrastructure.dto;

import java.time.Instant;
import java.util.Set;

public record UserDto(UserIdDto userId, String username, String firstName, String lastName, String email, String role,
                      String departmentCode, String language, Set<RolePermissionApi> rolePermissions,
                      Boolean deleted, OperatorIdDto operatorId, Instant createdAt, Instant updatedAt) {
}
