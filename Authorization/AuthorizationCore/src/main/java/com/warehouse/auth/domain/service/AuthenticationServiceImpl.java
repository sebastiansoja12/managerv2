package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.*;
import com.warehouse.commonassets.identificator.UserId;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    
    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenGenerator refreshTokenGenerator;

    @Override
    public RegisterResponse register(User user) {
        final UserResponse userResponse = userRepository.saveUser(user);
        return new RegisterResponse(userResponse);
    }

    @Override
    public LoginResponse login(User user) {
		final RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .expired(false)
                .revoked(false)
                .token(refreshTokenGenerator.generateToken(user))
                .build();
		final Token token = refreshTokenRepository.save(refreshToken);
		return new LoginResponse(token);
    }

    @Override
    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void logout(UserLogout userLogout) {
        refreshTokenRepository.delete(userLogout.getRefreshToken());
    }

    @Override
    public UserId nextUserId() {
        return new UserId((System.currentTimeMillis() % 900000) + 100000);
    }
}