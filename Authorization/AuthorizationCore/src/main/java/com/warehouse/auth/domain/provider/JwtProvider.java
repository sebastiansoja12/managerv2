package com.warehouse.auth.domain.provider;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("jwt")
public class JwtProvider {

    private static final String SECRET_KEY = "jwt.secret-key";

    private static final String EXPIRATION = "jwt.secret-key.expiration";

    private static final String REFRESH_TOKEN_EXPIRATION = "jwt.refresh-token.expiration";

    private String secretKey;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

}
