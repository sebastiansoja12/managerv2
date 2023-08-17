package com.warehouse.auth.domain.model;

import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class User {

    String username;

    String password;

    String firstName;

    String lastName;

    String email;

    Role role;

    String depotCode;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
}
