package com.warehouse.auth.domain.provider;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "application.jwt.security")
public class JwtProvider {
    private static final String SECRET_KEY = "application.jwt.security.secret-key";

    private static final String EXPIRATION = "application.jwt.security.secret-key.expiration";

    private static final String REFRESH_TOKEN_EXPIRATION = "application.jwt.security.refresh-token.expiration";

    private String secretKey;

    private String refreshTokenExpiration;

    private Long expiration;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setRefreshTokenExpiration(String refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

}
