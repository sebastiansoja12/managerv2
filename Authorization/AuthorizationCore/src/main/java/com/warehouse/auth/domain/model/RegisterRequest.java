package com.warehouse.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String depotCode;
}
