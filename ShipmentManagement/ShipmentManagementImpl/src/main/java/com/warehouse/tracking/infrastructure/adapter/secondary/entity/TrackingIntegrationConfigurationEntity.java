package com.warehouse.tracking.infrastructure.adapter.secondary.entity;

import java.util.UUID;

import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "tracking.TrackingIntegrationConfigurationEntity")
@Table(name = "tracking_integration_configurations")
public class TrackingIntegrationConfigurationEntity extends BelongsToOperator {

    @Id
    @Column(name = "tracking_integration_configuration_id", nullable = false)
    private UUID configurationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 32)
    private TrackingProviderId provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment", length = 32)
    private TrackingEnvironment environment;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "client_id", length = 512)
    private String clientId;

    @Column(name = "client_secret", length = 2048)
    private String encryptedClientSecret;

    @Column(name = "encrypted_configuration", length = 8192)
    private String encryptedConfiguration;

    public TrackingIntegrationConfigurationEntity() {
    }

    public TrackingIntegrationConfigurationEntity(final UUID configurationId,
                                                  final TrackingProviderId provider,
                                                  final TrackingEnvironment environment,
                                                  final boolean enabled,
                                                  final String clientId,
                                                  final String encryptedClientSecret,
                                                  final String encryptedConfiguration) {
        this.configurationId = configurationId;
        this.provider = provider;
        this.environment = environment;
        this.enabled = enabled;
        this.clientId = clientId;
        this.encryptedClientSecret = encryptedClientSecret;
        this.encryptedConfiguration = encryptedConfiguration;
    }

    public UUID getConfigurationId() {
        return configurationId;
    }

    public TrackingProviderId getProvider() {
        return provider;
    }

    public TrackingEnvironment getEnvironment() {
        return environment;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getClientId() {
        return clientId;
    }

    public String getEncryptedClientSecret() {
        return encryptedClientSecret;
    }

    public String getEncryptedConfiguration() {
        return encryptedConfiguration;
    }
}
