package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache.OperatorConfigurationCache;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper.OperatorConfigurationMapper;

import java.util.Optional;

public class OperatorConfigurationRepositoryImpl implements OperatorConfigurationRepository {

    private final OperatorConfigurationReadRepository repository;

    private final OperatorConfigurationCache cache;

    public OperatorConfigurationRepositoryImpl(final OperatorConfigurationReadRepository repository,
                                               final OperatorConfigurationCache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    @Override
    public Optional<OperatorConfiguration> findByOperatorId(final OperatorId operatorId) {
        return cache.find(operatorId)
                .or(() -> {
                    final Optional<OperatorConfiguration> configuration = repository.findById(operatorId)
                            .map(OperatorConfigurationMapper::toModel);
                    configuration.ifPresent(found -> cache.put(operatorId, found));
                    return configuration;
                });
    }

    @Override
    public OperatorConfiguration save(final OperatorId operatorId, final OperatorConfiguration configuration) {
        final OperatorConfiguration source = withStoredSections(operatorId, configuration);
        final OperatorConfiguration saved = repository.save(OperatorConfigurationMapper.toEntity(operatorId, source))
                .toModel();
        cache.evict(operatorId);
        return saved;
    }

    private OperatorConfiguration withStoredSections(final OperatorId operatorId,
                                                     final OperatorConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        final boolean missingSection = configuration.getDepartmentConfiguration() == null
                || configuration.getFleetConfiguration() == null
                || configuration.getCommunicationConfiguration() == null
                || configuration.getRegionalConfiguration() == null
                || configuration.getSortingConfiguration() == null
                || configuration.getPickupPointConfiguration() == null;
        if (!missingSection) {
            return configuration;
        }
        final Optional<OperatorConfiguration> stored = findByOperatorId(operatorId);
        OperatorConfiguration source = configuration;
        if (source.getDepartmentConfiguration() == null) {
            source = source.withDepartmentConfiguration(stored
                    .map(OperatorConfiguration::getDepartmentConfiguration)
                    .orElse(null));
        }
        if (source.getFleetConfiguration() == null) {
            source = source.withFleetConfiguration(stored
                    .map(OperatorConfiguration::getFleetConfiguration)
                    .orElse(null));
        }
        if (source.getCommunicationConfiguration() == null) {
            source = source.withCommunicationConfiguration(stored
                    .map(OperatorConfiguration::getCommunicationConfiguration)
                    .orElse(null));
        }
        if (source.getRegionalConfiguration() == null) {
            source = source.withRegionalConfiguration(stored
                    .map(OperatorConfiguration::getRegionalConfiguration)
                    .orElse(null));
        }
        if (source.getSortingConfiguration() == null) {
            source = source.withSortingConfiguration(stored
                    .map(OperatorConfiguration::getSortingConfiguration)
                    .orElse(null));
        }
        if (source.getPickupPointConfiguration() == null) {
            source = source.withPickupPointConfiguration(stored
                    .map(OperatorConfiguration::getPickupPointConfiguration)
                    .orElse(null));
        }
        return source;
    }
}
