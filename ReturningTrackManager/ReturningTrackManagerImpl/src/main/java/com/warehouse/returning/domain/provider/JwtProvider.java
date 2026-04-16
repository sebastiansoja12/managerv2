package com.warehouse.returning.domain.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotNull;



@ConfigurationProperties("jwt")
public class JwtProvider {

    @NotNull
    private String secretKey;

    @NotNull
    private Long expiration;

    @NotNull
    private String refreshTokenExpiration;

    public Long getExpiration() {
        return expiration;
    }

    public String getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public void setRefreshTokenExpiration(String refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
