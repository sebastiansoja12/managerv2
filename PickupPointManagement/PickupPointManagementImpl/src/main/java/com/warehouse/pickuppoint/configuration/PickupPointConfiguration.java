package com.warehouse.pickuppoint.configuration;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.pickuppoint.api.PickupPointApiService;
import com.warehouse.pickuppoint.application.port.primary.PickupPointPort;
import com.warehouse.pickuppoint.application.port.primary.PickupPointPortImpl;
import com.warehouse.pickuppoint.application.port.primary.PickupPointReadModelSyncPort;
import com.warehouse.pickuppoint.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointCoordinatesServicePort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointReadModelRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchRepository;
import com.warehouse.pickuppoint.application.service.PickupPointReadModelSyncServiceImpl;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.PickupPointApiServiceAdapter;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.mapper.PickupPointApiMapper;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.mapper.PickupPointWebMapper;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.DepartmentDirectoryServiceAdapter;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.PickupPointCoordinatesServiceAdapter;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.PickupPointReadModelRepositoryImpl;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.PickupPointRepositoryImpl;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.PickupPointSearchRepositoryImpl;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointReadEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.mapper.PickupPointPersistenceMapper;
import com.warehouse.voronoi.VoronoiCoordinatesService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PickupPointConfiguration {

    @Bean("pickupPointBaseRepository")
    public OperatorFilteredRepository<PickupPointEntity> pickupPointBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("pickupPointReadRepository")
    public OperatorFilteredRepository<PickupPointReadEntity> pickupPointReadRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public PickupPointPersistenceMapper pickupPointPersistenceMapper() {
        return new PickupPointPersistenceMapper();
    }

    @Bean
    public PickupPointRepository pickupPointRepository(
            @Qualifier("pickupPointBaseRepository")
            final OperatorFilteredRepository<PickupPointEntity> repository,
            final PickupPointPersistenceMapper persistenceMapper,
            final EntityManager entityManager) {
        return new PickupPointRepositoryImpl(repository, persistenceMapper, entityManager);
    }

    @Bean
    public PickupPointReadModelRepository pickupPointReadModelRepository(
            @Qualifier("pickupPointReadRepository")
            final OperatorFilteredRepository<PickupPointReadEntity> repository,
            final PickupPointPersistenceMapper persistenceMapper) {
        return new PickupPointReadModelRepositoryImpl(repository, persistenceMapper);
    }

    @Bean
    public PickupPointReadModelSyncPort pickupPointReadModelSyncPort(
            final PickupPointRepository pickupPointRepository,
            final PickupPointReadModelRepository pickupPointReadModelRepository) {
        return new PickupPointReadModelSyncServiceImpl(pickupPointRepository, pickupPointReadModelRepository);
    }

    @Bean
    public PickupPointSearchRepository pickupPointSearchRepository(
            @Qualifier("pickupPointReadRepository")
            final OperatorFilteredRepository<PickupPointReadEntity> repository) {
        return new PickupPointSearchRepositoryImpl(repository);
    }

    @Bean
    public DepartmentDirectoryServicePort pickupPointDepartmentDirectoryServicePort(
            final DepartmentApiService departmentApiService) {
        return new DepartmentDirectoryServiceAdapter(departmentApiService);
    }

    @Bean
    public PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort(
            @Qualifier("voronoiCoordinatesService") final VoronoiCoordinatesService coordinatesService) {
        return new PickupPointCoordinatesServiceAdapter(coordinatesService);
    }

    @Bean
    public PickupPointPort pickupPointPort(
            final PickupPointRepository pickupPointRepository,
            final PickupPointSearchRepository pickupPointSearchRepository,
            @Qualifier("pickupPointDepartmentDirectoryServicePort")
            final DepartmentDirectoryServicePort departmentDirectoryServicePort,
            final PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort,
            final DomainEventPublisher domainEventPublisher) {
        return new PickupPointPortImpl(
                pickupPointRepository,
                pickupPointSearchRepository,
                departmentDirectoryServicePort,
                pickupPointCoordinatesServicePort,
                domainEventPublisher,
                Clock.systemUTC());
    }

    @Bean
    public PickupPointApiMapper pickupPointApiMapper() {
        return new PickupPointApiMapper();
    }

    @Bean
    public PickupPointWebMapper pickupPointWebMapper() {
        return new PickupPointWebMapper();
    }

    @Bean
    public PickupPointApiService pickupPointApiService(
            final PickupPointPort pickupPointPort,
            final PickupPointApiMapper pickupPointApiMapper) {
        return new PickupPointApiServiceAdapter(pickupPointPort, pickupPointApiMapper);
    }
}
