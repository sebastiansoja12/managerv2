package com.warehouse.returning.infrastructure.adapter.secondary.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration("returning.routeLogProperties")
@ConfigurationProperties(prefix = "routelog")
public class RouteTrackerLogProperties {
    private String url;
    private String endpoint;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
