package com.warehouse.delivery.infrastructure.adapter.secondary.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties(prefix = "parcel")
public class ParcelStatusProperty {
    private String url;
    private String endpoint;
    private Long port;
}
