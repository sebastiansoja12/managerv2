package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletPlacementEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.StorageLocationEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.mapper.StorageLocationEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class StorageLocationRepositoryImpl implements StorageLocationRepository {

    private static final Logger log = LoggerFactory.getLogger(StorageLocationRepositoryImpl.class);

    private final OperatorFilteredRepository<StorageLocationEntity> repository;
    private final PalletPlacementReadRepository palletPlacementReadRepository;
    private final OperatorContextProvider operatorContextProvider;
    private final EntityManager entityManager;

    public StorageLocationRepositoryImpl(final OperatorFilteredRepository<StorageLocationEntity> repository,
                                         final PalletPlacementReadRepository palletPlacementReadRepository,
                                         final OperatorContextProvider operatorContextProvider,
                                         final EntityManager entityManager) {
        this.repository = repository;
        this.palletPlacementReadRepository = palletPlacementReadRepository;
        this.operatorContextProvider = operatorContextProvider;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public StorageLocation save(final StorageLocation storageLocation) {
        final StorageLocationEntity entity = StorageLocationEntityMapper.toEntity(storageLocation);
        try {
            if (storageLocation.getVersion() == null) {
                this.repository.create(entity);
            } else {
                this.repository.update(entity);
            }
            this.entityManager.flush();
        } catch (final OptimisticLockException | StaleStateException exception) {
            log.warn("Concurrent modification of storage location {}", storageLocation.getCode());
            throw concurrentModification(storageLocation);
        } catch (final PersistenceException exception) {
            if (exception.getCause() instanceof ConstraintViolationException
                    || exception instanceof ConstraintViolationException) {
                log.warn("Constraint violated while saving storage location {}", storageLocation.getCode());
                throw DepartmentMapException.conflict("PALLET_ALREADY_PLACED", "Pallet is already placed on "
                        + "another location or location " + storageLocation.getCode() + " was changed concurrently."
                        + " Reload the map and try again");
            }
            throw exception;
        }
        return byId(storageLocation.getLocationId()).one()
                .map(StorageLocationEntityMapper::toModel)
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StorageLocation> findById(final MapElementId locationId) {
        return byId(locationId).one().map(StorageLocationEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StorageLocation> findByMap(final DepartmentMapId departmentMapId) {
        return this.repository.createCriteria(StorageLocationEntity.class)
                .eq("departmentMapId.value", departmentMapId.value())
                .asc("code")
                .list()
                .stream()
                .map(StorageLocationEntityMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StorageLocation> findByPallet(final PalletTrackingNumber palletTrackingNumber) {
        return this.palletPlacementReadRepository.findByOperatorIdValueAndPalletTrackingNumberValue(
                        this.operatorContextProvider.currentOperatorId().getValue(), palletTrackingNumber.value())
                .map(PalletPlacementEntity::getLocationId)
                .flatMap(this::findById);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StorageLocation> findByPalletNumberFragment(final String fragment, final int limit) {
        final List<MapElementId> locationIds = this.palletPlacementReadRepository
                .findByOperatorIdValueAndPalletTrackingNumberValueContainingOrderByPalletTrackingNumberValueAsc(
                        this.operatorContextProvider.currentOperatorId().getValue(), fragment,
                        PageRequest.of(0, limit))
                .stream()
                .map(PalletPlacementEntity::getLocationId)
                .distinct()
                .toList();
        if (locationIds.isEmpty()) {
            return List.of();
        }
        return this.repository.createCriteria(StorageLocationEntity.class)
                .in("locationId.value", locationIds.stream().map(MapElementId::value).toList())
                .list()
                .stream()
                .map(StorageLocationEntityMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public void delete(final StorageLocation storageLocation) {
        byId(storageLocation.getLocationId()).one().ifPresent(this.repository::delete);
        this.entityManager.flush();
    }

    private Criteria<StorageLocationEntity> byId(final MapElementId locationId) {
        return this.repository.createCriteria(StorageLocationEntity.class).eq("locationId.value", locationId.value());
    }

    private static DepartmentMapException concurrentModification(final StorageLocation storageLocation) {
        return DepartmentMapException.conflict("LOCATION_CONCURRENT_MODIFICATION", "Location "
                + storageLocation.getCode() + " was changed by another user at the same time. Reload the map and try"
                + " again");
    }
}
