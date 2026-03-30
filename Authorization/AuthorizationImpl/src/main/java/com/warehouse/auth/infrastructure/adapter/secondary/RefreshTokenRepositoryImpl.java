package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.vo.Token;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RefreshTokenMapper;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Optional;


@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenReadRepository repository;

    private final RefreshTokenMapper refreshTokenMapper;

    private final JwtProvider jwtProvider;

    @Override
    public Token save(final RefreshToken refreshToken) {
        final RefreshTokenEntity entity = refreshTokenMapper.map(refreshToken);
        entity.setToken(hash(refreshToken.getToken()));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setExpiryDate(LocalDateTime.now().plus(Duration.ofMillis(jwtProvider.getRefreshTokenExpiration())));
        repository.save(entity);

        return new Token(refreshToken.getToken());
    }

    @Override
    public Optional<RefreshToken> find(final String token) {
        return repository.findByToken(hash(token)).map(refreshTokenMapper::map);
    }

    @Override
    public void delete(final String refreshToken) {
        repository.deleteByToken(hash(refreshToken));
    }

    @Override
    public void delete(final LocalDateTime time) {
        repository.deleteAllExpiredSince(time);
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
