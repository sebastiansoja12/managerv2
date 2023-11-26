package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "shipment")
public class ParcelProperty {

    private String url;

    private String name;

    private String endpoint;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }


}
