package com.warehouse.auth.infrastructure.adapter.primary.dto;

import java.time.Instant;
import java.util.Set;

public record UserDto(UserIdDto userId, String username, String firstName, String lastName, String email, String role,
                      String departmentCode, Set<RolePermissionApi> rolePermissions,
                      Boolean deleted, Instant createdAt, Instant updatedAt) {
}
