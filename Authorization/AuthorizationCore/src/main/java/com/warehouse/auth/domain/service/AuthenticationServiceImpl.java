package com.warehouse.auth.domain.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.UserResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    
    private final RefreshTokenRepository refreshTokenRepository;

    private final Long EXPIRY_TIME = 6000L;

    @Override
    public RegisterResponse register(User user) {
        final UserResponse userResponse = userRepository.signup(user);
        return new RegisterResponse(userResponse);
    }

    @Override
    public LoginResponse login(User user) {
		final RefreshToken refreshToken = RefreshToken.builder()
                .createdDate(Instant.now())
                .expiryDate(Instant.now().plus(ChronoUnit.HALF_DAYS.getDuration()))
				.token(UUID.randomUUID().toString())
                .username(user.getUsername())
                .build();
		final Token token = refreshTokenRepository.save(refreshToken);
		return new LoginResponse(token);
    }

    @Override
    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }
}