package com.warehouse.auth.infrastructure.adapter.primary.dto;

public record UserResponseDto(String username, String departmentCode, boolean nonExpired, boolean enabled,
		boolean nonLocked) {
}
