package com.warehouse.departmentmap.configuration;

import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.departmentmap.api.PalletLocationApiService;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPort;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPortImpl;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPort;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPortImpl;
import com.warehouse.departmentmap.application.port.secondary.CurrentUserServicePort;
import com.warehouse.departmentmap.application.port.secondary.DepartmentMapRepository;
import com.warehouse.departmentmap.application.port.secondary.DepartmentServicePort;
import com.warehouse.departmentmap.application.port.secondary.PalletMovementRepository;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.service.StorageLocationSynchronizer;
import com.warehouse.departmentmap.infrastructure.adapter.primary.PalletLocationApiServiceAdapter;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.DepartmentMapRepositoryImpl;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.DepartmentServiceAdapter;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.PalletMovementRepositoryImpl;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.PalletPlacementReadRepository;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.StorageLocationRepositoryImpl;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.DepartmentMapEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletMovementEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.StorageLocationEntity;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentMapConfiguration {

    @Bean
    public BaseRepository<DepartmentMapEntity> departmentMapEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public BaseRepository<StorageLocationEntity> departmentMapStorageLocationEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public BaseRepository<PalletMovementEntity> departmentMapPalletMovementEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DepartmentMapRepository departmentMapRepository(
            @Qualifier("departmentMapEntityRepository") final BaseRepository<DepartmentMapEntity> repository,
            final EntityManager entityManager) {
        return new DepartmentMapRepositoryImpl(repository, entityManager);
    }

    @Bean
    public StorageLocationRepository departmentMapStorageLocationRepository(
            @Qualifier("departmentMapStorageLocationEntityRepository") final BaseRepository<StorageLocationEntity> repository,
            final PalletPlacementReadRepository palletPlacementReadRepository,
            final OperatorContextProvider operatorContextProvider,
            final EntityManager entityManager) {
        return new StorageLocationRepositoryImpl(repository, palletPlacementReadRepository, operatorContextProvider,
                entityManager);
    }

    @Bean
    public PalletMovementRepository departmentMapPalletMovementRepository(
            @Qualifier("departmentMapPalletMovementEntityRepository") final BaseRepository<PalletMovementEntity> repository) {
        return new PalletMovementRepositoryImpl(repository);
    }

    @Bean
    public DepartmentServicePort departmentMapDepartmentServicePort(final DepartmentApiService departmentApiService) {
        return new DepartmentServiceAdapter(departmentApiService);
    }

    @Bean
    public CurrentUserServicePort departmentMapCurrentUserServicePort(
            final OperatorContextProvider operatorContextProvider) {
        return new CurrentUserServiceAdapter(operatorContextProvider);
    }

    @Bean
    public DepartmentMapPort departmentMapPort(final DepartmentMapRepository departmentMapRepository,
                                               final StorageLocationRepository departmentMapStorageLocationRepository,
                                               final DepartmentServicePort departmentMapDepartmentServicePort,
                                               final CurrentUserServicePort departmentMapCurrentUserServicePort) {
        return new DepartmentMapPortImpl(departmentMapRepository, departmentMapStorageLocationRepository,
                departmentMapDepartmentServicePort, departmentMapCurrentUserServicePort,
                new StorageLocationSynchronizer());
    }

    @Bean
    public PalletLocationPort palletLocationPort(final StorageLocationRepository departmentMapStorageLocationRepository,
                                                 final DepartmentMapRepository departmentMapRepository,
                                                 final PalletMovementRepository departmentMapPalletMovementRepository,
                                                 final CurrentUserServicePort departmentMapCurrentUserServicePort) {
        return new PalletLocationPortImpl(departmentMapStorageLocationRepository, departmentMapRepository,
                departmentMapPalletMovementRepository, departmentMapCurrentUserServicePort);
    }

    @Bean
    public PalletLocationApiService palletLocationApiService(final PalletLocationPort palletLocationPort) {
        return new PalletLocationApiServiceAdapter(palletLocationPort);
    }
}
