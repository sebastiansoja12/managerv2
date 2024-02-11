package com.warehouse.auth.infrastructure.adapter.primary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RefreshTokenRequestDto {
    @NotBlank
    String refreshToken;
    String username;
}
