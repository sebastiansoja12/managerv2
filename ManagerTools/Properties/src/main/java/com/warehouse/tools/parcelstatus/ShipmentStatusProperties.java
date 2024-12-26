package com.warehouse.tools.parcelstatus;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "parcel")
public class ShipmentStatusProperties {
    private String url;
    private String endpoint;
    private Long port;
}
