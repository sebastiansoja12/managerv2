package com.warehouse.auth.infrastructure.adapter.primary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    @NonNull
    private String email;

    private String username;
    @NonNull
    private String password;

    private String firstName;

    private String lastName;

    private String role;

    private String depotCode;
}
