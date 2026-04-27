package com.warehouse.auth.infrastructure.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(String authenticationToken, LoginResponseApi loginResponse) {
}
