package com.warehouse.shipment.domain.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

public class ShipmentReadModelSyncServiceImpl implements ShipmentReadModelSyncService {

    private final ShipmentReadModelRepository repository;

    private final OperatorFilteredRepository<ShipmentEntity> shipmentRepository;

    private final OperatorContext operatorContext;

    public ShipmentReadModelSyncServiceImpl(final ShipmentReadModelRepository repository,
                                            final OperatorFilteredRepository<ShipmentEntity> shipmentRepository,
                                            final OperatorContext operatorContext) {
        this.repository = repository;
        this.shipmentRepository = shipmentRepository;
        this.operatorContext = operatorContext;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sync(final ShipmentSnapshot snapshot) {
        this.repository.sync(snapshot);
    }

    @Transactional
    @Override
    public void syncReadModel(final ShipmentId shipmentId) {
        final ShipmentEntity entity = this.shipmentRepository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));

        if (entity.operatorId() == null) {
            throw new IllegalStateException("Shipment has no operator id");
        }

        final Shipment shipment = Shipment.from(entity);
        this.operatorContext.runAs(entity.operatorId(), () -> this.repository.sync(shipment.snapshot()));
    }

    @Transactional
    @Override
    public int syncReadModels() {
        final List<ShipmentEntity> shipments = shipmentRepository.createCriteria(ShipmentEntity.class)
                .list();

        shipments.stream()
                .map(Shipment::from)
                .map(Shipment::snapshot)
                .forEach(repository::sync);

        return shipments.size();
    }
}
