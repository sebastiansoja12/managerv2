package com.warehouse.paypal.domain.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "paypal")
public class PaypalConfigurationProperties {

    private String clientId;

    private String clientSecret;

    private String mode;

    private String url;


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}