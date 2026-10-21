package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPort;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPortImpl;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPort;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPortImpl;
import com.warehouse.departmentmap.application.port.secondary.CurrentUserServicePort;
import com.warehouse.departmentmap.application.port.secondary.DepartmentMapRepository;
import com.warehouse.departmentmap.application.port.secondary.PalletMovementRepository;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.service.StorageLocationSynchronizer;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.DepartmentMapEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletMovementEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.StorageLocationEntity;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackageClasses = PalletPlacementReadRepository.class)
@EntityScan(basePackageClasses = DepartmentMapEntity.class)
public class DepartmentMapPersistenceTestConfiguration {

    public static final OperatorId OPERATOR_ID = OperatorId.of(10L);
    public static final DepartmentId DEPARTMENT_ID = new DepartmentId(100L);

    @Bean
    public OperatorContextProvider operatorContextProvider() {
        return () -> Optional.of(new OperatorDetails(OPERATOR_ID, new UserId(7L), DEPARTMENT_ID));
    }

    @Bean
    public DepartmentMapRepository departmentMapRepository(final EntityManager entityManager,
                                                           final OperatorContextProvider operatorContextProvider) {
        return new DepartmentMapRepositoryImpl(
                new BaseRepository<DepartmentMapEntity>(entityManager, operatorContextProvider), entityManager);
    }

    @Bean
    public StorageLocationRepository storageLocationRepository(final EntityManager entityManager,
                                                               final OperatorContextProvider operatorContextProvider,
                                                               final PalletPlacementReadRepository palletPlacementReadRepository) {
        return new StorageLocationRepositoryImpl(
                new BaseRepository<StorageLocationEntity>(entityManager, operatorContextProvider),
                palletPlacementReadRepository, operatorContextProvider, entityManager);
    }

    @Bean
    public PalletMovementRepository palletMovementRepository(final EntityManager entityManager,
                                                             final OperatorContextProvider operatorContextProvider) {
        return new PalletMovementRepositoryImpl(
                new BaseRepository<PalletMovementEntity>(entityManager, operatorContextProvider));
    }

    @Bean
    public CurrentUserServicePort currentUserServicePort(final OperatorContextProvider operatorContextProvider) {
        return new CurrentUserServiceAdapter(operatorContextProvider);
    }

    @Bean
    public DepartmentMapPort departmentMapPort(final DepartmentMapRepository departmentMapRepository,
                                               final StorageLocationRepository storageLocationRepository,
                                               final CurrentUserServicePort currentUserServicePort) {
        return new DepartmentMapPortImpl(departmentMapRepository, storageLocationRepository,
                departmentId -> DEPARTMENT_ID.equals(departmentId), currentUserServicePort,
                new StorageLocationSynchronizer());
    }

    @Bean
    public PalletLocationPort palletLocationPort(final StorageLocationRepository storageLocationRepository,
                                                 final DepartmentMapRepository departmentMapRepository,
                                                 final PalletMovementRepository palletMovementRepository,
                                                 final CurrentUserServicePort currentUserServicePort) {
        return new PalletLocationPortImpl(storageLocationRepository, departmentMapRepository,
                palletMovementRepository, currentUserServicePort);
    }
}
