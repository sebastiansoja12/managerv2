package com.warehouse.tools.routelog;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "routelog")
public class RouteTrackerLogProperties {
    private String url;
    private String endpoint;
    private String initialize;
    private String returnTrackRequest;
    private String zebraVersionInformation;
    private String zebraIdInformation;
}
