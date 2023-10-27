package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.vo.Token;

public interface RefreshTokenRepository {

    Token save(RefreshToken refreshToken);

    RefreshToken validateRefreshToken(String token);

    void delete(String refreshToken);
}
