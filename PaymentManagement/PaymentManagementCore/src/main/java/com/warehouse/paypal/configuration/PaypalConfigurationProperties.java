package com.warehouse.paypal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
@ConfigurationProperties(prefix = "paypal")
public class PaypalConfigurationProperties {

    private String clientId;

    private String clientSecret;

    private String mode;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}