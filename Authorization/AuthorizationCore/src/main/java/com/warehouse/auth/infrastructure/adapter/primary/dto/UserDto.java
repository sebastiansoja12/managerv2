package com.warehouse.auth.infrastructure.adapter.primary.dto;

public record UserDto(UserIdDto userId, String username, String firstName, String lastName, String email, String role,
		String departmentCode) {
}
