package com.warehouse.auth.domain.vo;

public record AuthenticationResponse(String authenticationToken, LoginResponse loginResponse) {
}
