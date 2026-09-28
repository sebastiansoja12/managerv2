package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.event.UserLoggedOutEvent;
import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.vo.LoginResponse;
import com.warehouse.auth.domain.vo.Token;
import com.warehouse.auth.domain.vo.UsernamePasswordAuthentication;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenGenerator refreshTokenGenerator;

    private final UserRepository userRepository;

    private final Duration rotationGracePeriod;

    public AuthenticationServiceImpl(final RefreshTokenRepository refreshTokenRepository,
                                     final RefreshTokenGenerator refreshTokenGenerator,
                                     final UserRepository userRepository,
                                     final Duration rotationGracePeriod) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.userRepository = userRepository;
        this.rotationGracePeriod = rotationGracePeriod;
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
        final LocalDateTime now = LocalDateTime.now();
        final RefreshToken currentRefreshToken = refreshTokenRepository.find(refreshTokenValue)
                .orElseThrow(() -> new AuthenticationErrorException("Unknown refresh token"));
        if (!currentRefreshToken.isActual() || !currentRefreshToken.getExpiryDate().isAfter(now)) {
            throw new AuthenticationErrorException("Expired refresh token");
        }
        final OrganizationUser user = userRepository.findByUsername(currentRefreshToken.getUsername());
        if (user == null || Boolean.TRUE.equals(user.isDeleted())) {
            throw new AuthenticationErrorException("Refresh token owner is missing or deleted");
        }

        // Rotated token stays valid for a short grace period, so concurrent refreshes (several browser tabs,
        // parallel terminal requests, a lost response) don't end the session.
        refreshTokenRepository.expireAt(refreshTokenValue, now.plus(rotationGracePeriod));
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
        final OrganizationUser user = this.userRepository.findByUsername(refreshToken.getUsername());
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
        final UsernameTenantPasswordAuthenticationToken authentication =
                (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getOperatorId();
    }
}
