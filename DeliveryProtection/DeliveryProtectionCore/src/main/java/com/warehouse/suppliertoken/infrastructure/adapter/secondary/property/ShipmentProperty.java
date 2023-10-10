package com.warehouse.suppliertoken.infrastructure.adapter.secondary.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "shipment")
public class ShipmentProperty {

    String url;

    public void setUrl(String url) {
        this.url = url;
    }

}
