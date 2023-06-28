package com.warehouse.auth.domain.provider;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "application.jwt.security")
public class JwtProvider {
    private static final String SECRET_KEY = "application.jwt.security.secret-key";

    private static final String EXPIRATION = "application.jwt.security.expiration";

    private static final String REFRESH_TOKEN = "application.jwt.security.refresh-token";

    private String secretKey;

    private String refreshToken;

    private Long expiration;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

}
