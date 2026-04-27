package com.warehouse.auth.infrastructure.dto;

public record RegisterRequestDto(String email, String username, String password, String firstName, String lastName,
		String role, String departmentCode) {

}
