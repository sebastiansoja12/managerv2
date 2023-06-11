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

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Generating token for user: {}", loginRequest.getUsername());

        return authenticationService.login(authentication);
    }

    @Override
    public void signup(RegisterRequest registerRequest) {
        authenticationService.signup(registerRequest);
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        authenticationService.logout(refreshTokenRequest.getRefreshToken());
        log.info("Token of user: " + refreshTokenRequest.getUsername() + " has been successfully deleted" +
                ". Logging out");
    }

    @Override
    public List<User> findCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationService.findCurrentUser(authentication.getName());
    }

    @Override
    public User findUserByUsername(String username) {
        return authenticationService.findCurrentUser(username).get(0);
    }
}
