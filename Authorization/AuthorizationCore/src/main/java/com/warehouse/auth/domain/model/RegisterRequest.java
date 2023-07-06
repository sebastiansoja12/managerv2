package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NonNull
    private String email;

    private String username;
    @NonNull
    private String password;

    private String firstName;

    private String lastName;

    private String role;

    private String depotCode;

    public String getUsername() {
        if (StringUtils.isEmpty(username)) {
            throw new AuthenticationErrorException("Username cannot be empty");
        }
        return username;
    }
}
