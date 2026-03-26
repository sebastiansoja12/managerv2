package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final OperatorFilteredRepository<ShipmentEntity> repository;

    public ShipmentRepositoryImpl(final OperatorFilteredRepository<ShipmentEntity> repository) {
        this.repository = repository;
    }

    @Override
    public void createOrUpdate(final Shipment shipment) {
        final ShipmentEntity entity = ShipmentEntity.from(shipment);
        if (exists(shipment.getShipmentId())) {
            repository.update(entity);
        } else {
            repository.create(entity);
        }
    }

    @Override
    public Shipment findById(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return null;
        }
        return repository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .map(Shipment::from)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return shipmentId != null && repository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .isPresent();
    }

    @Override
    public Optional<Shipment> findByExternalId(final ExternalId<String> externalId) {
        return repository.createCriteria(ShipmentEntity.class)
                .eq("externalId.value", externalId.value())
                .one()
                .map(Shipment::from);
    }

    @Override
    public Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId) {
        return repository.createCriteria(ShipmentEntity.class)
                .eq("externalId.value", externalId.value())
                .one()
                .map(ShipmentEntity::getShipmentId);
    }

    @Override
    public Shipment findByTrackingNumber(final TrackingNumber trackingNumber) {
        return repository.createCriteria(ShipmentEntity.class)
                .eq("trackingNumber.value", trackingNumber.value())
                .one()
                .map(Shipment::from).orElseThrow(
                () -> new ShipmentNotFoundException("Shipment was not found")
        );
    }
}
