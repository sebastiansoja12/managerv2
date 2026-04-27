package com.warehouse.auth.infrastructure.dto;

public record UserResponseDto(String username, String departmentCode, boolean nonExpired, boolean enabled,
		boolean nonLocked) {
}
