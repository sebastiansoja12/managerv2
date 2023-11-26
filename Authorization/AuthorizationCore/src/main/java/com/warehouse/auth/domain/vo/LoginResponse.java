package com.warehouse.auth.domain.vo;

import lombok.Value;

@Value
public class LoginResponse {
    Token refreshToken;
}
