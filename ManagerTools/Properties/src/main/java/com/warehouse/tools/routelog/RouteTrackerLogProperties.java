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
    private String deliveryReturnRequest;
    private String zebraVersionInformation;
    private String zebraIdInformation;
    private String supplierCode;
    private String depotCode;
    private String username;
    private String zebraInitialize;
    private String delivery;
    private String terminalRequest;
}
