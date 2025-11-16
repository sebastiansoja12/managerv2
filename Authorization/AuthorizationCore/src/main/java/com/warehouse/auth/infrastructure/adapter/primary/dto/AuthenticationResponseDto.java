package com.warehouse.auth.infrastructure.adapter.primary.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(String authenticationToken, LoginResponseApi loginResponse) {
}
