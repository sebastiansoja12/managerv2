package com.warehouse.auth.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    String username;

    String password;

    String firstName;

    String lastName;

    String email;

    String role;

    String depotCode;
}
