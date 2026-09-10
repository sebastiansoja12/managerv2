package com.warehouse.supplierconfiguration.configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.SupplierConfigurationApiService;
import com.warehouse.supplierconfiguration.application.port.primary.SupplierConfigurationPort;
import com.warehouse.supplierconfiguration.application.port.primary.SupplierConfigurationPortImpl;
import com.warehouse.supplierconfiguration.application.port.secondary.CurrentUserServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationHistoryRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationPolicyRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.SupplierConfigurationApiServiceAdapter;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.SupplierConfigurationHistoryRepositoryImpl;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.SupplierConfigurationPolicyRepositoryImpl;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.SupplierConfigurationRepositoryImpl;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.SupplierDirectoryServiceAdapter;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationChangeEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationPolicyEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.mapper.SupplierConfigurationPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class SupplierConfigurationContextConfiguration {

    @Bean("supplierConfigurationBaseRepository")
    public OperatorFilteredRepository<SupplierConfigurationEntity> supplierConfigurationBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("supplierConfigurationPolicyBaseRepository")
    public OperatorFilteredRepository<SupplierConfigurationPolicyEntity> supplierConfigurationPolicyBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("supplierConfigurationChangeBaseRepository")
    public OperatorFilteredRepository<SupplierConfigurationChangeEntity> supplierConfigurationChangeBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public SupplierConfigurationPersistenceMapper supplierConfigurationPersistenceMapper() {
        return new SupplierConfigurationPersistenceMapper();
    }

    @Bean
    public SupplierConfigurationRepository supplierConfigurationRepository(
            @Qualifier("supplierConfigurationBaseRepository")
            final OperatorFilteredRepository<SupplierConfigurationEntity> repository,
            final SupplierConfigurationPersistenceMapper persistenceMapper,
            final EntityManager entityManager) {
        return new SupplierConfigurationRepositoryImpl(repository, persistenceMapper, entityManager);
    }

    @Bean
    public SupplierConfigurationPolicyRepository supplierConfigurationPolicyRepository(
            @Qualifier("supplierConfigurationPolicyBaseRepository")
            final OperatorFilteredRepository<SupplierConfigurationPolicyEntity> repository,
            final SupplierConfigurationPersistenceMapper persistenceMapper,
            final EntityManager entityManager) {
        return new SupplierConfigurationPolicyRepositoryImpl(repository, persistenceMapper, entityManager);
    }

    @Bean
    public SupplierConfigurationHistoryRepository supplierConfigurationHistoryRepository(
            @Qualifier("supplierConfigurationChangeBaseRepository")
            final OperatorFilteredRepository<SupplierConfigurationChangeEntity> repository,
            final SupplierConfigurationPersistenceMapper persistenceMapper) {
        return new SupplierConfigurationHistoryRepositoryImpl(repository, persistenceMapper);
    }

    @Bean("supplierConfiguration.supplierDirectoryServicePort")
    public SupplierDirectoryServicePort supplierDirectoryServicePort(final SupplierApiService supplierApiService) {
        return new SupplierDirectoryServiceAdapter(supplierApiService);
    }

    @Bean("supplierConfiguration.currentUserServicePort")
    public CurrentUserServicePort currentUserServicePort(final CurrentUserApiService currentUserApiService) {
        return new CurrentUserServiceAdapter(currentUserApiService);
    }

    @Bean
    public SupplierConfigurationPort supplierConfigurationPort(
            final SupplierConfigurationRepository supplierConfigurationRepository,
            final SupplierConfigurationPolicyRepository supplierConfigurationPolicyRepository,
            final SupplierConfigurationHistoryRepository supplierConfigurationHistoryRepository,
            @Qualifier("supplierConfiguration.supplierDirectoryServicePort")
            final SupplierDirectoryServicePort supplierDirectoryServicePort,
            @Qualifier("supplierConfiguration.currentUserServicePort")
            final CurrentUserServicePort currentUserServicePort) {
        return new SupplierConfigurationPortImpl(
                supplierConfigurationRepository,
                supplierConfigurationPolicyRepository,
                supplierConfigurationHistoryRepository,
                supplierDirectoryServicePort,
                currentUserServicePort,
                Clock.systemUTC());
    }

    @Bean
    public SupplierConfigurationApiService supplierConfigurationApiService(
            final SupplierConfigurationPort supplierConfigurationPort) {
        return new SupplierConfigurationApiServiceAdapter(supplierConfigurationPort);
    }
}
