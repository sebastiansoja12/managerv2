package com.warehouse.returning.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "shipment")
public class ShipmentProperties {

    private String url;

    private String name;

    private String endpoint;
}