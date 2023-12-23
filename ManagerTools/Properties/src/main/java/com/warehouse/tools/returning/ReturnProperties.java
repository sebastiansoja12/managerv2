package com.warehouse.tools.returning;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "return")
public class ReturnProperties {

    private String url;

    private String endpoint;
}