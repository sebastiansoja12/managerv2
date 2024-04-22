package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class User {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private String depotCode;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
}
