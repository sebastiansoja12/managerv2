package com.warehouse.shipment.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "software")
public class SoftwareConfigurationProperties {
    private String url;
    private String endpoint;
}
