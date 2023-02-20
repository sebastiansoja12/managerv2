package com.warehouse.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    String firstName;

    String lastName;

    String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String role;

    Depot depot;
}
