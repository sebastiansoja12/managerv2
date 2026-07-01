package com.warehouse.auth.infrastructure.dto;

public record CurrentUserAuthenticationDto(
        String jwtToken,
        UserDto user) {
}
