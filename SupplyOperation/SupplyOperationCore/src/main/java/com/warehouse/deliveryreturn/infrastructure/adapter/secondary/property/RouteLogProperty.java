package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "route")
public class RouteLogProperty {
    private String url;
    private String endpoint;
    private Long port;
}
