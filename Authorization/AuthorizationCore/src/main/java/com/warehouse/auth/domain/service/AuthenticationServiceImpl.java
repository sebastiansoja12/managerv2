package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.vo.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.port.secondary.UserRepository;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RegisterResponse register(User user) {
        final UserResponse userResponse = userRepository.signup(user);
        return new RegisterResponse(userResponse);
    }

    @Override
    public LoginResponse login(Authentication authentication) {
		final RefreshToken refreshToken = RefreshToken.builder()
                .createdDate(Instant.now())
				.token(UUID.randomUUID().toString())
                .build();
		final Token token = refreshTokenRepository.save(refreshToken);
		return new LoginResponse(token);
    }
}