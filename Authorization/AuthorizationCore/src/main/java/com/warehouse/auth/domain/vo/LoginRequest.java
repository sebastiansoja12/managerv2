package com.warehouse.auth.domain.vo;


import com.warehouse.auth.infrastructure.adapter.primary.dto.LoginRequestDto;


public record LoginRequest(String username, String password) {

	public static LoginRequest from(final LoginRequestDto loginRequest) {
		return new LoginRequest(loginRequest.username(), loginRequest.password());
	}
}
