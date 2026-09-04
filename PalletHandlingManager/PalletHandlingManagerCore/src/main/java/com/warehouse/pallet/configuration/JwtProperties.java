package com.warehouse.pallet.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(String secretKey, String issuer, String audience) {

    public JwtProperties {
        if (isMissing(secretKey) || isMissing(issuer) || isMissing(audience)) {
            throw new IllegalStateException(
                    "JWT configuration is incomplete: set JWT_SECRET_KEY, JWT_ISSUER and JWT_AUDIENCE");
        }
    }

    private static boolean isMissing(final String value) {
        return value == null || value.isBlank() || value.contains("${");
    }
}
