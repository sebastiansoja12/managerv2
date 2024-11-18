package com.warehouse.tools.softwareconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "software")
public class SoftwareConfigurationProperties {
    private String url;
    private String endpoint;
}
