package com.warehouse.geocoding.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.commonassets.model.BelongsToOperator;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "geocoding.GeocodingConfigurationEntity")
@Table(name = "geocoding_configurations")
public class GeocodingConfigurationEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "geocoding_configuration_id", nullable = false))
    private GeocodingConfigurationId geocodingConfigurationId;

    @Column(name = "api_url", length = 50)
    private String apiUrl;

    @Column(name = "api_user_name")
    private String apiUserName;

    @Column(name = "api_password", length = 50)
    private String apiPassword;

    @Column(name = "api_key", length = 50)
    private String apiKey;

    @Column(name = "access_token", length = 50)
    private String accessToken;

    @Column(name = "refresh_token", length = 50)
    private String refreshToken;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private GeocodingProvider provider;

    public GeocodingConfigurationEntity() {
    }

    public GeocodingConfigurationEntity(final GeocodingConfigurationId geocodingConfigurationId,
                                         final String apiUrl,
                                         final String apiUserName,
                                         final String apiPassword,
                                         final String apiKey,
                                         final String accessToken,
                                         final String refreshToken,
                                         final boolean enabled,
                                         final GeocodingProvider provider) {
        this.geocodingConfigurationId = geocodingConfigurationId;
        this.apiUrl = apiUrl;
        this.apiUserName = apiUserName;
        this.apiPassword = apiPassword;
        this.apiKey = apiKey;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.enabled = enabled;
        this.provider = provider;
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

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public GeocodingProvider getProvider() {
        return provider;
    }
}
