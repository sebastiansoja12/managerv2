package com.warehouse.auth.domain.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@ConfigurationProperties("jwt")
public class JwtProvider {

    @NotNull
    private String secretKey;

    @NotNull
    private Long expiration;

    @NotNull
    private Long refreshTokenExpiration;

    @NotNull
    private String issuer;

    @NotNull
    private String audience;

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpiration(final Long expiration) {
        this.expiration = expiration;
    }

    public void setRefreshTokenExpiration(final Long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
