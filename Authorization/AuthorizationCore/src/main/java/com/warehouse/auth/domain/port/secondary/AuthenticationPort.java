package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthenticationPort {

    AuthenticationResponse login(Authentication authentication, String token);

    void signup(RegisterRequest registerRequest);

    void logout(String token);

    List<User> findCurrentUser(String username);
}
