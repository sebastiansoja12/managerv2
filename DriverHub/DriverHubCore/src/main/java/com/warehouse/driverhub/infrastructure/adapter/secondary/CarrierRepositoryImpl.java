package com.warehouse.driverhub.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.driverhub.application.port.secondary.CarrierRepository;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierDriverEntity;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierEntity;
import com.warehouse.driverhub.infrastructure.adapter.secondary.mapper.CarrierEntityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CarrierRepositoryImpl implements CarrierRepository {

    private final OperatorFilteredRepository<CarrierEntity> repository;
    private final CarrierDriverReadRepository driverReadRepository;

    public CarrierRepositoryImpl(final OperatorFilteredRepository<CarrierEntity> repository,
                                 final CarrierDriverReadRepository driverReadRepository) {
        this.repository = repository;
        this.driverReadRepository = driverReadRepository;
    }

    @Override
    @Transactional
    public Carrier save(final Carrier carrier) {
        final CarrierEntity entity = CarrierEntityMapper.toEntity(carrier);
        if (byId(carrier.getCarrierId()).one().isPresent()) {
            this.repository.update(entity);
        } else {
            this.repository.create(entity);
        }
        return carrier;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrier> findById(final CarrierId carrierId, final OperatorId operatorId) {
        return byId(carrierId)
                .one()
                .filter(entity -> Objects.equals(entity.operatorId(), operatorId))
                .map(CarrierEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrier> findActiveById(final CarrierId carrierId, final OperatorId operatorId) {
        return byId(carrierId)
                .isTrue("active")
                .one()
                .filter(entity -> Objects.equals(entity.operatorId(), operatorId))
                .map(CarrierEntityMapper::toActiveModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrier> findByDriverId(final DriverId driverId, final OperatorId operatorId) {
        return this.driverReadRepository.findById(driverId)
                .map(CarrierDriverEntity::getCarrierId)
                .flatMap(carrierId -> findById(carrierId, operatorId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carrier> findAll(final OperatorId operatorId, final boolean activeOnly) {
        final Criteria<CarrierEntity> criteria = this.repository.createCriteria(CarrierEntity.class);
        if (activeOnly) {
            criteria.isTrue("active");
        }
        return criteria.asc("name")
                .list()
                .stream()
                .filter(entity -> Objects.equals(entity.operatorId(), operatorId))
                .map(CarrierEntityMapper::toModel)
                .toList();
    }

    private Criteria<CarrierEntity> byId(final CarrierId carrierId) {
        return this.repository.createCriteria(CarrierEntity.class).eq("carrierId.value", carrierId.getValue());
    }
}
