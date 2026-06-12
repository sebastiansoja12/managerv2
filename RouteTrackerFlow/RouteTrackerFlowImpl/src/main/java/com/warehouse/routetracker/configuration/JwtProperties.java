package com.warehouse.routetracker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("jwt")
public record JwtProperties(@NotBlank String secretKey,
                            @NotBlank String issuer,
                            @NotBlank String audience) {
}
