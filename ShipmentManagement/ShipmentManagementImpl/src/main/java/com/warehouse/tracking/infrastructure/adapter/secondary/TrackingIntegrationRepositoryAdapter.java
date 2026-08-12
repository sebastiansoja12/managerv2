package com.warehouse.tracking.infrastructure.adapter.secondary;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.port.secondary.TrackingIntegrationRepository;
import com.warehouse.tracking.infrastructure.adapter.secondary.entity.TrackingIntegrationConfigurationEntity;

public class TrackingIntegrationRepositoryAdapter implements TrackingIntegrationRepository {

    private final OperatorFilteredRepository<TrackingIntegrationConfigurationEntity> repository;
    private final CredentialCipher credentialCipher;
    private final ObjectMapper objectMapper;

    public TrackingIntegrationRepositoryAdapter(
            final OperatorFilteredRepository<TrackingIntegrationConfigurationEntity> repository,
            final CredentialCipher credentialCipher,
            final ObjectMapper objectMapper) {
        this.repository = repository;
        this.credentialCipher = credentialCipher;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<TrackingIntegrationConfiguration> findByProvider(final TrackingProviderId providerId) {
        return repository.createCriteria(TrackingIntegrationConfigurationEntity.class)
                .eq("provider", providerId)
                .one()
                .map(entity -> TrackingIntegrationConfigurationMapper.toModel(entity, credentialCipher, objectMapper));
    }

    @Override
    public void create(final TrackingIntegrationConfiguration configuration) {
        repository.create(TrackingIntegrationConfigurationMapper.toEntity(
                configuration, credentialCipher, objectMapper));
    }

    @Override
    public void update(final TrackingIntegrationConfiguration configuration) {
        repository.update(TrackingIntegrationConfigurationMapper.toEntity(
                configuration, credentialCipher, objectMapper));
    }
}
