package com.warehouse.geocoding.domain.model;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationSnapshot;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;

import java.util.Objects;

public class GeocodingConfiguration extends BelongsToOperator {

    private GeocodingConfigurationId geocodingConfigurationId;
    private String apiUrl;
    private String apiUserName;
    private String apiPassword;
    private String apiKey;
    private String clientNumber;
    private String accessToken;
    private String refreshToken;
    private boolean enabled;
    private boolean defaultProvider;
    private GeocodingProvider provider;

    public GeocodingConfiguration(final GeocodingConfigurationId geocodingConfigurationId,
                                  final String apiUrl,
                                  final String apiUserName,
                                  final String apiPassword,
                                  final String apiKey,
                                  final String clientNumber,
                                  final String accessToken,
                                  final String refreshToken,
                                  final boolean enabled,
                                  final boolean defaultProvider,
                                  final GeocodingProvider provider) {
        this.geocodingConfigurationId = geocodingConfigurationId;
        this.apiUrl = apiUrl;
        this.apiUserName = apiUserName;
        this.apiPassword = apiPassword;
        this.apiKey = apiKey;
        this.clientNumber = clientNumber;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.enabled = enabled;
        this.defaultProvider = defaultProvider;
        this.provider = Objects.requireNonNull(provider, "Geocoding provider is required");
    }

    public void update(final GeocodingConfigurationUpdateCommand command) {
        this.provider = Objects.requireNonNull(command.provider(), "Geocoding provider is required");
        this.apiUrl = command.provider().getUrl();
        this.apiUserName = command.apiUserName();
        this.apiPassword = command.apiPassword();
        this.apiKey = command.apiKey();
        this.clientNumber = command.clientNumber();
        this.accessToken = command.accessToken();
        this.refreshToken = command.refreshToken();
        this.enabled = command.enabled();
        this.defaultProvider = command.defaultProvider();
    }

    public void unsetDefaultProvider() {
        this.defaultProvider = false;
    }

    public GeocodingConfigurationSnapshot toSnapshot() {
        return new GeocodingConfigurationSnapshot(
                geocodingConfigurationId,
                apiUrl,
                apiUserName,
                apiPassword,
                apiKey,
                clientNumber,
                accessToken,
                refreshToken,
                enabled,
                defaultProvider,
                provider);
    }

    public GeocodingConfigurationId getGeocodingConfigurationId() {
        return geocodingConfigurationId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiUserName() {
        return apiUserName;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDefaultProvider() {
        return defaultProvider;
    }

    public GeocodingProvider getProvider() {
        return provider;
    }
}
