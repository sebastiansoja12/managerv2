package com.warehouse.deliverynetwork.configuration;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.deliverynetwork.api.DeliveryNetworkApiService;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPortImpl;
import com.warehouse.deliverynetwork.application.port.secondary.DeliveryNetworkRepository;
import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.domain.service.DeliveryPathFinder;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.DeliveryNetworkApiServiceAdapter;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkApiMapper;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkEditorApiMapper;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet.DeliveryNetworkSpreadsheetService;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.DeliveryNetworkRepositoryImpl;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.DepartmentDirectoryServiceAdapter;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkEntity;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper.DeliveryNetworkPersistenceMapper;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper.DepartmentDirectoryMapper;
import com.warehouse.department.api.DepartmentApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryNetworkConfiguration {

    @Bean("deliveryNetworkBaseRepository")
    public BaseRepository<DeliveryNetworkEntity> deliveryNetworkBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DeliveryNetworkRepository deliveryNetworkRepository(
            @Qualifier("deliveryNetworkBaseRepository")
            final BaseRepository<DeliveryNetworkEntity> repository,
            final DeliveryNetworkPersistenceMapper persistenceMapper) {
        return new DeliveryNetworkRepositoryImpl(repository, persistenceMapper);
    }

    @Bean
    public DeliveryNetworkPersistenceMapper deliveryNetworkPersistenceMapper() {
        return new DeliveryNetworkPersistenceMapper();
    }

    @Bean
    public DepartmentDirectoryMapper departmentDirectoryMapper() {
        return new DepartmentDirectoryMapper();
    }

    @Bean
    public DepartmentDirectoryServicePort departmentDirectoryServicePort(
            final DepartmentApiService departmentApiService,
            final DepartmentDirectoryMapper departmentDirectoryMapper) {
        return new DepartmentDirectoryServiceAdapter(departmentApiService, departmentDirectoryMapper);
    }

    @Bean
    public DeliveryPathFinder deliveryPathFinder() {
        return new DeliveryPathFinder();
    }

    @Bean
    public DeliveryNetworkApiMapper deliveryNetworkApiMapper() {
        return new DeliveryNetworkApiMapper();
    }

    @Bean
    public DeliveryNetworkEditorApiMapper deliveryNetworkEditorApiMapper() {
        return new DeliveryNetworkEditorApiMapper();
    }

    @Bean
    public DeliveryNetworkSpreadsheetService deliveryNetworkSpreadsheetService() {
        return new DeliveryNetworkSpreadsheetService();
    }

    @Bean
    public DeliveryNetworkPort deliveryNetworkPort(
            final DeliveryNetworkRepository deliveryNetworkRepository,
            final DepartmentDirectoryServicePort departmentDirectoryServicePort,
            final OperatorContextProvider operatorContextProvider,
            final DeliveryPathFinder deliveryPathFinder,
            final DomainEventPublisher domainEventPublisher) {
        return new DeliveryNetworkPortImpl(
                deliveryNetworkRepository,
                departmentDirectoryServicePort,
                operatorContextProvider,
                deliveryPathFinder,
                domainEventPublisher);
    }

    @Bean
    public DeliveryNetworkApiService deliveryNetworkApiService(
            final DeliveryNetworkPort deliveryNetworkPort,
            final DeliveryNetworkApiMapper deliveryNetworkApiMapper) {
        return new DeliveryNetworkApiServiceAdapter(deliveryNetworkPort, deliveryNetworkApiMapper);
    }
}
