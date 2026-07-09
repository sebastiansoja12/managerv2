package com.warehouse.terminal.domain.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "device.pair")
public class DevicePairKeyProvider {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
