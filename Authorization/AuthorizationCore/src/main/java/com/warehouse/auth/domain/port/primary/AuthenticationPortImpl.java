package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.UserLogout;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.infrastructure.adapter.secondary.Logger;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final Logger logger;


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {

        final User user = findUser(loginRequest.getUsername());

        final String authenticationToken = jwtService.generateToken(user);

        authenticationService.login(user);

        return AuthenticationResponse.builder()
                .authenticationToken(authenticationToken)
                .build();
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
                .role(mapRole(request.getRole()))
                .depotCode(request.getDepotCode())
                .build();

        return authenticationService.register(user);
    }

    @Override
    public User findUser(String username) {
        return authenticationService.findUser(username);
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {

        final UserLogout userLogout = UserLogout.builder()
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .username(refreshTokenRequest.getUsername())
                .build();

        authenticationService.logout(userLogout);

        logLogoutInformation(refreshTokenRequest.getUsername());
    }

    private Role mapRole(String value) {
        final String role = value.toUpperCase();
        return switch (role) {
            case "ADMIN" -> Role.ADMIN;
            case "MANAGER" -> Role.MANAGER;
            default -> Role.USER;
        };
    }

    private void logLogoutInformation(String username) {
        logger.info("User {} has been successfully logged out", username);
    }

    private void handleRequest(RegisterRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new AuthenticationErrorException("Request is not correct");
        }
    }


}
