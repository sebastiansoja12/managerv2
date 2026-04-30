package com.warehouse.auth.domain.vo;

public record LoginResponse(Token refreshToken, String username) {
}
