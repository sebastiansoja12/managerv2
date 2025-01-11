package com.warehouse.commonassets.enumeration;

public enum SoftwareConfigurationUrl {
    REROUTE_TRACKER_URL("reroute-tracker-url"),
    ROUTE_TRACKER_URL("route-tracker-url"),
    ROUTE_TRACKER_INITIALIZE_URL("route-tracker-initialize-url"),
    DEVICE_VALIDATION("device-validation");


    private final String url;

    SoftwareConfigurationUrl(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
