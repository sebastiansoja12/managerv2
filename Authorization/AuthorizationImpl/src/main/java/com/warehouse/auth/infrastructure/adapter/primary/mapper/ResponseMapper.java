package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.dto.OperatorIdDto;
import com.warehouse.auth.infrastructure.dto.RolePermissionApi;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ResponseMapper {

    public static UserDto map(final User user) {
        if (user == null) {
            return null;
        }
        final UserIdDto userId = new UserIdDto(user.getUserId().value());
        final String username = user.getUsername();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        final String email = user.getEmail();
        final String role = user.getRole().name();
        final String departmentCode = user.getDepartmentCode().getValue();
        final String language = user.getLanguage();
        final Boolean deleted = user.isDeleted();
        final Instant createdAt = user.createdAt();
        final Instant updatedAt = user.updatedAt();
		final Set<RolePermissionApi> rolePermissions = user.getPermissions().stream()
				.map(rolePermission -> new RolePermissionApi(
						rolePermission.getPermission().getPermission()))
				.collect(Collectors.toSet());
        final OperatorIdDto operatorId = new OperatorIdDto(user.getOperatorIdValue());
		return new UserDto(userId, username, firstName, lastName, email, role, departmentCode, language, rolePermissions, deleted,
                operatorId, createdAt, updatedAt);
    }
}
