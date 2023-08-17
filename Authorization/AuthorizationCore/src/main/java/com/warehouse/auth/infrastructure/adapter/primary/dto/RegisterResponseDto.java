package com.warehouse.auth.infrastructure.adapter.primary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    UserResponseDto userRegisterResponse;
}
