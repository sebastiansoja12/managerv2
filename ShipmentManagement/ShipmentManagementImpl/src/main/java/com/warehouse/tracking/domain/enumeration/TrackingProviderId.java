package com.warehouse.tracking.domain.enumeration;

public enum TrackingProviderId {
    INPOST("InPost");

    private final String displayName;

    TrackingProviderId(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
