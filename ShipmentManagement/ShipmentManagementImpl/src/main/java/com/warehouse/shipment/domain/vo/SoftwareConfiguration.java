package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.infrastructure.adapter.secondary.api.SoftwareConfigurationDto;

public class SoftwareConfiguration {
    private final String value;
    private final String url;

    public SoftwareConfiguration(final String value, final String url) {
        this.value = value;
        this.url = url;
    }

    public static SoftwareConfiguration from(final SoftwareConfigurationDto body) {
        return new SoftwareConfiguration(body.getValue(), body.getUrl());
    }

    public String getValue() {
        return value;
    }

    public String getUrl() {
        return url;
    }

    // TODO
    public String getApiKey() {
        return "api-key";
    }
}
