package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.AuthenticationPort;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.List;

@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationPort authenticationPort;

    private final JwtProvider jwtProvider;

    @Override
    public AuthenticationResponse login(Authentication authentication) {
        final String token = jwtProvider.generateToken(authentication);
        return authenticationPort.login(authentication, token);
    }

    @Override
    public void signup(RegisterRequest registerRequest) {
        authenticationPort.signup(registerRequest);
    }

    @Override
    public void logout(String token) {
        authenticationPort.logout(token);
    }

    @Override
    public List<User> findCurrentUser(String username) {
        return authenticationPort.findCurrentUser(username);
    }

}
