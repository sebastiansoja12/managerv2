package com.warehouse.returning.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.ReturnPackageNotFoundException;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageToEntityMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageToModelMapper;

public class ReturningRepositoryImpl implements ReturnRepository {

    private final ReturnReadRepository repository;

    public ReturningRepositoryImpl(final ReturnReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReturnPackage findById(final ReturnPackageId returnPackageId) {
		return this.repository
				.findById(ReturnId.of(returnPackageId))
				.map(ReturnPackageToModelMapper::map)
                .orElseThrow(ReturnPackageNotFoundException::new);
    }

    @Override
    public ReturnPackage findByShipmentId(final ShipmentId shipmentId) {
        final Optional<ReturnPackageEntity> returnPackage = this.repository.findByShipmentId(shipmentId);
        return returnPackage.map(ReturnPackageToModelMapper::map).orElse(null);
    }

    @Override
    public void createOrUpdate(final ReturnPackage returnPackage) {
        final ReturnPackageEntity returnPackageEntity = ReturnPackageToEntityMapper.map(returnPackage);
        this.repository.save(returnPackageEntity);
    }

    @Override
    public boolean existsForShipment(final ShipmentId shipmentId) {
        final Optional<ReturnPackageEntity> returnPackage = this.repository.findByShipmentId(shipmentId);
        return returnPackage.isPresent();
    }
}
