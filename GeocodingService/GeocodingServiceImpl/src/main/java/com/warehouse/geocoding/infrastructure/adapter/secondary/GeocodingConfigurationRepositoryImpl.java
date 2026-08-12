package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.port.secondary.GeocodingRepository;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;

public class GeocodingConfigurationRepositoryImpl implements GeocodingRepository {

    private final OperatorFilteredRepository<GeocodingConfigurationEntity> repository;
    private final CredentialCipher credentialCipher;

    public GeocodingConfigurationRepositoryImpl(
            final OperatorFilteredRepository<GeocodingConfigurationEntity> repository,
            final CredentialCipher credentialCipher) {
        this.repository = repository;
        this.credentialCipher = credentialCipher;
    }

    @Override
    public void create(final GeocodingConfiguration configuration) {
        repository.create(GeocodingConfigurationMapper.toEntity(configuration, credentialCipher));
    }

    @Override
    public void update(final GeocodingConfiguration configuration) {
        repository.update(GeocodingConfigurationMapper.toEntity(configuration, credentialCipher));
    }

    @Override
    @Transactional
    public void delete(final GeocodingConfigurationId geocodingConfigurationId) {
        repository.createCriteria(GeocodingConfigurationEntity.class)
                .eq("geocodingConfigurationId.value", geocodingConfigurationId.value())
                .one()
                .ifPresent(repository::delete);
    }

    @Override
    public Optional<GeocodingConfiguration> findById(
            final GeocodingConfigurationId geocodingConfigurationId) {
        return repository.createCriteria(GeocodingConfigurationEntity.class)
                .eq("geocodingConfigurationId.value", geocodingConfigurationId.value())
                .one()
                .map(entity -> GeocodingConfigurationMapper.toModel(entity, credentialCipher));
    }

    @Override
    public Optional<GeocodingConfiguration> findByProvider(final GeocodingProvider provider) {
        return repository.createCriteria(GeocodingConfigurationEntity.class)
                .eq("provider", provider)
                .one()
                .map(entity -> GeocodingConfigurationMapper.toModel(entity, credentialCipher));
    }

    @Override
    public List<GeocodingConfiguration> findAll() {
        return repository.createCriteria(GeocodingConfigurationEntity.class)
                .list()
                .stream()
                .map(entity -> GeocodingConfigurationMapper.toModel(entity, credentialCipher))
                .toList();
    }
}
