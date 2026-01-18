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
    private String terminalVersionInformation;
    private String terminalIdInformation;
    private String supplierCode;
    private String depotCode;
    private String username;
    private String delivery;
    private String terminalRequest;
    private String shipment;
    private String shipmentPerson;
}
