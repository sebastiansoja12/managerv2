package com.warehouse.auth.infrastructure.adapter.primary.dto;

public record RegisterRequestDto(String email, String username, String password, String firstName, String lastName,
		String role, String departmentCode) {

}
