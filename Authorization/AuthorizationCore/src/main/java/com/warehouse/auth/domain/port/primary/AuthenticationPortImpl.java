package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.service.JwtService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
		final Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        final String authenticationToken = jwtService.generateToken((UserDetails) authenticate.getDetails());

        final LoginResponse loginResponse = authenticationService.login(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(authenticationToken)
                .refreshToken(loginResponse.getRefreshToken().getValue())
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
                .role(request.getRole())
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
        log.info("Token of user: " + refreshTokenRequest.getUsername() + " has been successfully deleted" +
                ". Logging out");
    }

    private void handleRequest(RegisterRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new AuthenticationErrorException("Request is not correct");
        }
    }
}
