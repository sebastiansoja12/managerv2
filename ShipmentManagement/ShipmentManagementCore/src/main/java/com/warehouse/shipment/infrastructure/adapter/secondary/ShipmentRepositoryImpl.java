package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

import java.util.Optional;

public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentReadRepository repository;

    public ShipmentRepositoryImpl(final ShipmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createOrUpdate(final Shipment shipment) {
        final ShipmentEntity entity = ShipmentEntity.from(shipment);
        repository.save(entity);
    }

    @Override
    public Shipment findById(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return null;
        }
        return repository.findByShipmentId(shipmentId)
                .map(Shipment::from)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return shipmentId != null && repository.existsById(shipmentId);
    }

    @Override
    public Optional<Shipment> findByExternalId(final ExternalId<String> externalId) {
        final Optional<ShipmentEntity> entity = repository.findByExternalId(externalId);
        return entity.map(Shipment::from);
    }

    @Override
    public Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId) {
        final Optional<ShipmentEntity> entity = repository.findByExternalId(externalId);
        return entity.map(ShipmentEntity::getShipmentId);
    }

    @Override
    public Shipment findByTrackingNumber(final TrackingNumber trackingNumber) {
        return repository.findByTrackingNumber(trackingNumber).map(Shipment::from).orElseThrow(
                () -> new ShipmentNotFoundException("Shipment was not found")
        );
    }
}
