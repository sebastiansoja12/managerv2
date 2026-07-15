package com.warehouse.commonassets.enumeration;

import java.util.List;

public enum GeocodingProvider {
    POSITION_STACK(
            "https://api.positionstack.com/v1/forward",
            List.of(ConfigurationField.API_KEY),
            List.of(ProviderApi.GEOCODING_API)),
    GEOAPIFY(
            "https://api.geoapify.com/v1/geocode/search",
            List.of(ConfigurationField.API_KEY),
            List.of(ProviderApi.GEOCODING_API));

    private final String url;
    private final List<ConfigurationField> activeFields;
    private final List<ProviderApi> providerApis;

    GeocodingProvider(final String url,
                      final List<ConfigurationField> activeFields,
                      final List<ProviderApi> providerApis) {
        this.url = url;
        this.activeFields = List.copyOf(activeFields);
        this.providerApis = List.copyOf(providerApis);
    }

    public String getUrl() {
        return url;
    }

    public List<ConfigurationField> getActiveFields() {
        return activeFields;
    }

    public List<ProviderApi> getProviderApis() {
        return providerApis;
    }

    public enum ConfigurationField {
        API_USER_NAME,
        API_PASSWORD,
        API_KEY,
        CLIENT_NUMBER,
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    public enum ProviderApi {
        GEOCODING_API
    }
}
