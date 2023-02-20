package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.LoginRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthenticationService {

    AuthenticationResponse login(Authentication authentication);

    void signup(RegisterRequest registerRequest);

    void logout(String token);

    List<User> findCurrentUser(String username);
}
