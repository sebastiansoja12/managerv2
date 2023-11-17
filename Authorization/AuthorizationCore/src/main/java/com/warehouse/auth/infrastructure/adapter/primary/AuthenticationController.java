package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
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

    private final AuthenticationRequestMapper requestMapper;

    private final AuthenticationResponseMapper responseMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        final LoginRequest request = requestMapper.map(loginRequest);
        final AuthenticationResponse response = authenticationPort.login(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequestDto registerRequest) {
        final RegisterRequest request = requestMapper.map(registerRequest);
        final RegisterResponse registerResponse = authenticationPort.signup(request);
        return new ResponseEntity<>(responseMapper.map(registerResponse), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        final RefreshTokenRequest request = requestMapper.map(refreshTokenRequest);
        authenticationPort.logout(request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        final User user = authenticationPort.findUser(username);
        return new ResponseEntity<>(responseMapper.map(user), HttpStatus.OK);
    }
}
