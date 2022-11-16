package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.LoginRequest;
import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
@Slf4j
public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Generating token for user: {}", loginRequest.getUsername());

        return authenticationService.login(authentication);
    }

    @Override
    public void signup(RegisterRequest registerRequest) {

    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        authenticationService.logout(refreshTokenRequest.getRefreshToken());
        log.info("Token of user: " + refreshTokenRequest.getUsername() + " has been successfully deleted" +
                ". Logging out");
    }
}
