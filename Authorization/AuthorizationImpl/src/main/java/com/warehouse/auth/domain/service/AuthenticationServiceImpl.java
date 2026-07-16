package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.event.UserLoggedOutEvent;
import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.vo.LoginResponse;
import com.warehouse.auth.domain.vo.Token;
import com.warehouse.auth.domain.vo.UsernamePasswordAuthentication;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;

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
        return new LoginResponse(token, usernamePasswordAuthentication.username());
    }

    @Override
    @Transactional
    public LoginResponse refresh(final String refreshTokenValue) {
        final RefreshToken currentRefreshToken = refreshTokenRepository.find(refreshTokenValue)
                .filter(RefreshToken::isActual)
                .filter(refreshToken -> refreshToken.getExpiryDate().isAfter(java.time.LocalDateTime.now()))
                .orElseThrow(() -> new AuthenticationErrorException("Invalid or expired refresh token"));
        final User user = userRepository.findByUsername(currentRefreshToken.getUsername());
        if (user == null || Boolean.TRUE.equals(user.isDeleted())) {
            throw new AuthenticationErrorException("Invalid or expired refresh token");
        }

        refreshTokenRepository.delete(refreshTokenValue);
        final RefreshToken rotatedRefreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .expired(false)
                .revoked(false)
                .token(refreshTokenGenerator.generateToken(user))
                .build();
        final Token rotatedToken = refreshTokenRepository.save(rotatedRefreshToken);
        return new LoginResponse(rotatedToken, user.getUsername());
    }

    @Override
    @Transactional
    public void logout(final String refreshTokenValue) {
        final RefreshToken refreshToken = refreshTokenRepository.find(refreshTokenValue).orElse(null);
        if (refreshToken == null) {
            return;
        }

        refreshTokenRepository.delete(refreshTokenValue);
        final User user = this.userRepository.findByUsername(refreshToken.getUsername());
        if (user != null) {
            user.markAsLoggedOut();
            this.userRepository.createOrUpdate(user);
            DomainRegistry.eventPublisher().publishEvent(new UserLoggedOutEvent(user.snapshot()));
        }
    }

    @Override
    public UserId currentUserId() {
        return (UserId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public OperatorId currentOperatorId() {
        return (OperatorId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
