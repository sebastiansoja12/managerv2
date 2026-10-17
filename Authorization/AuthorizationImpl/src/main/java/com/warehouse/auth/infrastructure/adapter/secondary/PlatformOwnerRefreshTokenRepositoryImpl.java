package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.PlatformOwnerRefreshTokenRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.PlatformOwnerRefreshTokenEntity;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

public class PlatformOwnerRefreshTokenRepositoryImpl implements PlatformOwnerRefreshTokenRepository {

    private static final int REFRESH_TOKEN_SIZE_BYTES = 48;

    private final PlatformOwnerRefreshTokenReadRepository repository;

    private final JwtProvider jwtProvider;

    private final SecureRandom secureRandom;

    public PlatformOwnerRefreshTokenRepositoryImpl(final PlatformOwnerRefreshTokenReadRepository repository,
                                                   final JwtProvider jwtProvider) {
        this.repository = repository;
        this.jwtProvider = jwtProvider;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String create(final PlatformOwnerId platformOwnerId) {
        final byte[] randomBytes = new byte[REFRESH_TOKEN_SIZE_BYTES];
        secureRandom.nextBytes(randomBytes);
        final String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        final LocalDateTime now = LocalDateTime.now();
        repository.save(PlatformOwnerRefreshTokenEntity.builder()
                .token(hash(refreshToken))
                .platformOwnerId(platformOwnerId)
                .createdDate(now)
                .expiryDate(now.plus(Duration.ofMillis(jwtProvider.getRefreshTokenExpiration())))
                .build());
        return refreshToken;
    }

    @Override
    public Optional<PlatformOwnerId> findActivePlatformOwner(final String refreshToken) {
        return repository.findByToken(hash(refreshToken))
                .filter(token -> token.getExpiryDate().isAfter(LocalDateTime.now()))
                .map(PlatformOwnerRefreshTokenEntity::getPlatformOwnerId);
    }

    @Override
    public void delete(final String refreshToken) {
        repository.deleteByToken(hash(refreshToken));
    }

    @Override
    public void deleteAll(final PlatformOwnerId platformOwnerId) {
        repository.deleteAllByPlatformOwnerId(platformOwnerId.value());
    }

    private String hash(final String refreshToken) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", exception);
        }
    }
}
