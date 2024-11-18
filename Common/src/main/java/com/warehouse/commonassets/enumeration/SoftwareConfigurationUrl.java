package com.warehouse.commonassets.enumeration;

public enum SoftwareConfigurationUrl {
    REROUTE_TRACKER_URL("reroute-tracker-url"),
    ROUTE_TRACKER_URL("route-tracker-url"),
    ROUTE_TRACKER_INITIALIZE_URL("route-tracker-initialize-url");


    private final String url;

    SoftwareConfigurationUrl(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
