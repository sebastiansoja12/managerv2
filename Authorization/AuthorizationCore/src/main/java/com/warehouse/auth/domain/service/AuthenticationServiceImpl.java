package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.LoginResponse;
import com.warehouse.auth.domain.vo.Token;
import com.warehouse.auth.domain.vo.UsernamePasswordAuthentication;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenGenerator refreshTokenGenerator;

    private final UserRepository userRepository;

    public AuthenticationServiceImpl(final RefreshTokenRepository refreshTokenRepository,
                                     final RefreshTokenGenerator refreshTokenGenerator,
                                     final UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse login(final UsernamePasswordAuthentication usernamePasswordAuthentication) {
        final RefreshToken refreshToken = RefreshToken.builder()
                .username(usernamePasswordAuthentication.username())
                .expired(false)
                .revoked(false)
                .token(refreshTokenGenerator.generateToken(userRepository.findByUsername(usernamePasswordAuthentication.username())))
                .build();
        final Token token = refreshTokenRepository.save(refreshToken);
        return new LoginResponse(token);
    }

    @Override
    public void logout(final UserId userId, final String token) {
        final User user = this.userRepository.findById(userId);
        user.markAsLoggedOut();
        this.userRepository.createOrUpdate(user);
    }

    @Override
    public UserId currentUserId() {
        return (UserId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
