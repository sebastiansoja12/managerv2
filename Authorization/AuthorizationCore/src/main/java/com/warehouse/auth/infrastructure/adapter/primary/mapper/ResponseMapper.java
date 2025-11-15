package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.primary.dto.UserDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.UserIdDto;

public abstract class ResponseMapper {

    public static UserDto map(final User user) {
        final UserIdDto userId = new UserIdDto(user.getUserId().value());
        final String username = user.getUsername();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        final String email = user.getEmail();
        final String role = user.getRole().name();
        final String departmentCode = user.getDepartmentCode().getValue();
        return new UserDto(userId, username, firstName, lastName, email, role, departmentCode);
    }
}
