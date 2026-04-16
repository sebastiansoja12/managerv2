package com.warehouse.auth.domain.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("apikey")
public class ApiKeyProvider {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
