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
    private String expiration;

    @NotNull
    private String refreshTokenExpiration;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public void setRefreshTokenExpiration(String refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
