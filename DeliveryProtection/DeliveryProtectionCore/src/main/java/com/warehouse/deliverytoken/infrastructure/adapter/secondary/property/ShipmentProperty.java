package com.warehouse.deliverytoken.infrastructure.adapter.secondary.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "shipment")
public class ShipmentProperty {

    private String url;

    private String name;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
