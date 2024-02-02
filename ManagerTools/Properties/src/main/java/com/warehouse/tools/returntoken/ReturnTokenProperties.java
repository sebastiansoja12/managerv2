package com.warehouse.tools.returntoken;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "returntoken")
public class ReturnTokenProperties {

    private String url;

    private String endpoint;
}