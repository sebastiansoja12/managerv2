package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.vo.Token;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    Token refreshToken;
}
