package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.vo.Token;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository {

    Token save(final RefreshToken refreshToken);

    Optional<RefreshToken> find(final String token);

    void delete(final String refreshToken);

    void delete(final LocalDateTime time);

}
