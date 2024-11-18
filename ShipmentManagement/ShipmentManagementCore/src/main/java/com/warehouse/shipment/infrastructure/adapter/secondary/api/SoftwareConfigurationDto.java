package com.warehouse.shipment.infrastructure.adapter.secondary.api;

public class SoftwareConfigurationDto {
    private final String value;
    private final String url;

    public SoftwareConfigurationDto(final String value, final String url) {
        this.value = value;
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public String getUrl() {
        return url;
    }
}
