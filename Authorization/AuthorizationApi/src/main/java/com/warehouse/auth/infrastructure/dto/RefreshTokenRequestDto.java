package com.warehouse.auth.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequestDto {
    @NotBlank
    private String refreshToken;
    private String username;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
