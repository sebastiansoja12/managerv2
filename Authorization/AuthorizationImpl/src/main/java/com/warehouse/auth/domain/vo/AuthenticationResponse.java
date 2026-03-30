package com.warehouse.auth.domain.vo;

public record AuthenticationResponse(String accessToken, String refreshToken) {
}
