package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;


    private AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));


        log.info("Generating token for user: {}", loginRequest.getUsername());

        return AuthenticationResponse.builder().build();
    }

    @Override
    public void signup(RegisterRequest registerRequest) {

    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {

        log.info("Token of user: " + refreshTokenRequest.getUsername() + " has been successfully deleted" +
                ". Logging out");
    }
}
