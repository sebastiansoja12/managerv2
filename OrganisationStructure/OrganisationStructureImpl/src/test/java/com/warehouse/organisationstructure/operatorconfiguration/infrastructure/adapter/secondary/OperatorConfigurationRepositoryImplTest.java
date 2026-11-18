package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CommunicationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache.OperatorConfigurationCache;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.OperatorConfigurationEntity;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper.OperatorConfigurationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorConfigurationRepositoryImplTest {

    @Mock
    private OperatorConfigurationReadRepository readRepository;

    @Mock
    private OperatorConfigurationCache cache;

    private OperatorConfigurationRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new OperatorConfigurationRepositoryImpl(readRepository, cache);
    }

    @Test
    void shouldReturnCachedConfigurationWithoutQueryingDatabase() {
        final OperatorConfiguration configuration = OperatorTestFixtures.configuration();
        when(cache.find(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.of(configuration));

        assertThat(repository.findByOperatorId(OperatorTestFixtures.OPERATOR_ID)).containsSame(configuration);
        verifyNoInteractions(readRepository);
    }

    @Test
    void shouldLoadConfigurationFromDatabaseAndCacheItOnMiss() {
        when(cache.find(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.empty());
        when(readRepository.findById(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.of(
                OperatorConfigurationMapper.toEntity(OperatorTestFixtures.OPERATOR_ID,
                        OperatorTestFixtures.configuration())));

        final Optional<OperatorConfiguration> configuration =
                repository.findByOperatorId(OperatorTestFixtures.OPERATOR_ID);

        assertThat(configuration).isPresent();
        verify(cache).put(OperatorTestFixtures.OPERATOR_ID, configuration.get());
    }

    @Test
    void shouldNotCacheMissingConfiguration() {
        when(cache.find(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.empty());
        when(readRepository.findById(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.empty());

        assertThat(repository.findByOperatorId(OperatorTestFixtures.OPERATOR_ID)).isEmpty();
        verify(cache, never()).put(any(), any());
    }

    @Test
    void shouldEvictCacheAfterSave() {
        final OperatorConfigurationEntity entity = OperatorConfigurationMapper.toEntity(
                OperatorTestFixtures.OPERATOR_ID, OperatorTestFixtures.configuration());
        when(readRepository.save(any(OperatorConfigurationEntity.class))).thenReturn(entity);

        repository.save(OperatorTestFixtures.OPERATOR_ID, OperatorTestFixtures.configuration());

        verify(cache).evict(OperatorTestFixtures.OPERATOR_ID);
    }

    @Test
    void shouldKeepStoredSectionsMissingFromSavedConfiguration() {
        final FleetConfiguration storedFleet = new FleetConfiguration(14, 21, 8, 30, false, 15, 33);
        final RegionalConfiguration storedRegional = RegionalConfiguration.defaultConfiguration();
        final CommunicationConfiguration storedCommunication = CommunicationConfiguration.defaultConfiguration();
        when(cache.find(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.of(OperatorTestFixtures.configuration()
                .withFleetConfiguration(storedFleet)
                .withCommunicationConfiguration(storedCommunication)
                .withRegionalConfiguration(storedRegional)));
        when(readRepository.save(any(OperatorConfigurationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final OperatorConfiguration saved = repository.save(OperatorTestFixtures.OPERATOR_ID,
                OperatorTestFixtures.configuration());

        assertThat(saved.getFleetConfiguration().getDefaultVehiclePalletCapacity()).isEqualTo(33);
        assertThat(saved.getFleetConfiguration().isRequirePreTripChecklist()).isFalse();
        assertThat(saved.getRegionalConfiguration().getTimeZone()).isEqualTo(storedRegional.getTimeZone());
    }
}
