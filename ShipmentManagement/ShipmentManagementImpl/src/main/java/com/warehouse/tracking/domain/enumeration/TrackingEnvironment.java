package com.warehouse.tracking.domain.enumeration;

public enum TrackingEnvironment {
    STAGE("https://stage-api.inpost-group.com"),
    PRODUCTION("https://api.inpost-group.com");

    private final String baseUrl;

    TrackingEnvironment(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getTokenUrl() {
        return baseUrl + "/oauth2/token";
    }
}
