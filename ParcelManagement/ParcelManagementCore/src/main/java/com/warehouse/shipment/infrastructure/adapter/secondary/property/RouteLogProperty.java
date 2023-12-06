package com.warehouse.shipment.infrastructure.adapter.secondary.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "routelog")
public class RouteLogProperty {
    private String url;
    private String endpoint;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
