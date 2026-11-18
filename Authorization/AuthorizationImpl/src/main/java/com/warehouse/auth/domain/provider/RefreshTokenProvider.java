package com.warehouse.auth.domain.provider;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("refreshtoken")
public class RefreshTokenProvider {

    @NotNull
    private String key;

    private Duration rotationGracePeriod = Duration.ofSeconds(60);

    public void setKey(String key) {
        this.key = key;
    }
}
