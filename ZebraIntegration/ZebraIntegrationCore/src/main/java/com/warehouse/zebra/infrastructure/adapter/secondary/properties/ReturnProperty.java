package com.warehouse.zebra.infrastructure.adapter.secondary.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "return")
public class ReturnProperty {

    private String url;

    private String endpoint;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
