package com.warehouse.auth.infrastructure.dto;

import java.time.Instant;
import java.util.Set;

public record CurrentUserProfileDto(
        UserIdDto userId,
        String username,
        String firstName,
        String lastName,
        String email,
        String role,
        String departmentCode,
        String language,
        String apiKey,
        Set<RolePermissionApi> rolePermissions,
        Boolean deleted,
        OperatorIdDto operatorId,
        Instant createdAt,
        Instant updatedAt) {
}
