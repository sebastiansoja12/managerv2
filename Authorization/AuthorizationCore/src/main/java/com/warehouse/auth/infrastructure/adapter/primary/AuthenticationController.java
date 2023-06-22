package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationPort authenticationPort;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationPort.login(loginRequest);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody RegisterRequest registerRequest) {
        authenticationPort.signup(registerRequest);
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authenticationPort.logout(refreshTokenRequest);
    }

    @GetMapping("/current-user")
    public List<User> currentUser() {
        return null;
    }

    @GetMapping("/{username}")
    public User findUserByUsername(@PathVariable String username) {
        return null;
    }
}
