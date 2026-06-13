package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;

import java.util.List;
import java.util.Optional;

public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final OperatorFilteredRepository<ShipmentEntity> writeRepository;
    private final ShipmentPersistenceMapper persistenceMapper;

    public ShipmentRepositoryImpl(final OperatorFilteredRepository<ShipmentEntity> writeRepository,
                                  final ShipmentPersistenceMapper persistenceMapper) {
        this.writeRepository = writeRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void createOrUpdate(final Shipment shipment) {
        final ShipmentEntity entity = this.persistenceMapper.toEntity(shipment);
        if (writeModelExists(shipment.getShipmentId())) {
            writeRepository.update(entity);
        } else {
            writeRepository.create(entity);
        }
    }

    @Override
    public Shipment findById(final ShipmentId shipmentId) {
        return writeRepository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .map(this.persistenceMapper::toDomain)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return shipmentId != null && writeRepository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .isPresent();
    }

    @Override
    public Optional<Shipment> findByExternalId(final ExternalId<String> externalId) {
        return writeRepository.createCriteria(ShipmentEntity.class)
                .eq("externalId.value", externalId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId) {
        return writeRepository.createCriteria(ShipmentEntity.class)
                .eq("externalId.value", externalId.value())
                .one()
                .map(ShipmentEntity::getShipmentId);
    }

    @Override
    public Shipment findByTrackingNumber(final TrackingNumber trackingNumber) {
        return writeRepository.createCriteria(ShipmentEntity.class)
                .eq("trackingNumber.value", trackingNumber.value())
                .one()
                .map(this.persistenceMapper::toDomain)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));
    }

    @Override
    public List<Shipment> findAll() {
        return this.writeRepository.createCriteria(ShipmentEntity.class)
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
    }

    private boolean writeModelExists(final ShipmentId shipmentId) {
        return shipmentId != null && writeRepository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .isPresent();
    }
}
