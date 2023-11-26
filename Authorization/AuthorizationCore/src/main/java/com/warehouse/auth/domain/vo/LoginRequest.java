package com.warehouse.auth.domain.vo;


import lombok.Value;

@Value
public class LoginRequest {
	String username;
	String password;
}
