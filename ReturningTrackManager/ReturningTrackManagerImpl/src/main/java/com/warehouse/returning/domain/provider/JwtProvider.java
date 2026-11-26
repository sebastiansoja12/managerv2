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

    private String issuer = "manager-v2";

    private String audience = "manager-v2-gui";

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(final String audience) {
        this.audience = audience;
    }

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
