package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.vo.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    UserResponse userRegisterResponse;
}
