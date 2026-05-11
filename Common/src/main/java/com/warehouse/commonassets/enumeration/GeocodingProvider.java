package com.warehouse.commonassets.enumeration;

import java.util.List;

public enum GeocodingProvider {
    POSITION_STACK(
            "https://api.positionstack.com/v1/forward",
            List.of(ConfigurationField.API_KEY));

    private final String url;
    private final List<ConfigurationField> activeFields;

    GeocodingProvider(final String url, final List<ConfigurationField> activeFields) {
        this.url = url;
        this.activeFields = List.copyOf(activeFields);
    }

    public String getUrl() {
        return url;
    }

    public List<ConfigurationField> getActiveFields() {
        return activeFields;
    }

    public enum ConfigurationField {
        API_USER_NAME,
        API_PASSWORD,
        API_KEY,
        CLIENT_NUMBER,
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }
}
