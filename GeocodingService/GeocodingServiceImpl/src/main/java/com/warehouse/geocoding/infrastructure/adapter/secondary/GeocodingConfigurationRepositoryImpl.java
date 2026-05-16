package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.port.secondary.GeocodingRepository;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;

public class GeocodingConfigurationRepositoryImpl implements GeocodingRepository {

    private final OperatorFilteredRepository<GeocodingConfigurationEntity> repository;
    private final GeocodingPasswordCipher passwordCipher;

    public GeocodingConfigurationRepositoryImpl(
            final OperatorFilteredRepository<GeocodingConfigurationEntity> repository,
            final GeocodingPasswordCipher passwordCipher) {
        this.repository = repository;
        this.passwordCipher = passwordCipher;
    }

    @Override
    public void create(final GeocodingConfiguration configuration) {
        repository.create(GeocodingConfigurationMapper.toEntity(configuration, passwordCipher));
    }

    @Override
    public void update(final GeocodingConfiguration configuration) {
        repository.update(GeocodingConfigurationMapper.toEntity(configuration, passwordCipher));
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
                .map(entity -> GeocodingConfigurationMapper.toModel(entity, passwordCipher));
    }

    @Override
    public Optional<GeocodingConfiguration> findByProvider(final GeocodingProvider provider) {
        return repository.createCriteria(GeocodingConfigurationEntity.class)
                .eq("provider", provider)
                .one()
                .map(entity -> GeocodingConfigurationMapper.toModel(entity, passwordCipher));
    }

    @Override
    public List<GeocodingConfiguration> findAll() {
        return repository.createCriteria(GeocodingConfigurationEntity.class)
                .list()
                .stream()
                .map(entity -> GeocodingConfigurationMapper.toModel(entity, passwordCipher))
                .toList();
    }
}
