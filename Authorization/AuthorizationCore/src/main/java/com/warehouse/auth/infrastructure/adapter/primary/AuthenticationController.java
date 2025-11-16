package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.service.ApiKeyService;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.infrastructure.adapter.primary.dto.LoginRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RefreshTokenRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RegisterRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationRequestMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationPort authenticationPort;

    private final ApiKeyService apiKeyService;

    private final AuthenticationRequestMapper requestMapper;

    private final AuthenticationResponseMapper responseMapper;

    @PostMapping("/signup")
    public ResponseEntity<?> create(
            @RequestHeader("X-API-KEY") final String apiKey,
            @RequestBody final RegisterRequestDto registerRequest) {
        apiKeyService.validateApiKey(apiKey);
        final RegisterRequest request = RegisterRequest.from(registerRequest);
        final RegisterResponse registerResponse = this.authenticationPort.signup(request);
        return new ResponseEntity<>(responseMapper.map(registerResponse), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginRequestDto loginRequest) {
        final LoginRequest request = LoginRequest.from(loginRequest);
        final AuthenticationResponse response = authenticationPort.login(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody final RefreshTokenRequestDto refreshTokenRequest) {
        final RefreshTokenRequest request = requestMapper.map(refreshTokenRequest);
        authenticationPort.logout(request);
        return ResponseEntity.ok().build();
    }
}
