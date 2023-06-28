package com.warehouse.auth.domain.port.primary;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.service.AuthenticationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {

        return AuthenticationResponse.builder().build();
    }

    @Override
    public RegisterResponse signup(RegisterRequest request) {

        handleRequest(request);

        final var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .depotCode(request.getDepotCode())
                .build();

        return authenticationService.register(user);
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {

        log.info("Token of user: " + refreshTokenRequest.getUsername() + " has been successfully deleted" +
                ". Logging out");
    }

    private void handleRequest(RegisterRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new AuthenticationErrorException("Request is not correct");
        }
    }
}
