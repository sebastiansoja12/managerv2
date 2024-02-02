package com.warehouse.tools.supplier;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "supplier")
public class SupplierValidatorProperties {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private String endpoint;
}
