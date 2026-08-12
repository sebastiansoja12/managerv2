package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

import java.util.Optional;

public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final OperatorFilteredRepository<ShipmentEntity> writeRepository;

    public ShipmentRepositoryImpl(final OperatorFilteredRepository<ShipmentEntity> writeRepository) {
        this.writeRepository = writeRepository;
    }

    @Override
    public void createOrUpdate(final Shipment shipment) {
        final ShipmentEntity entity = ShipmentEntity.from(shipment);
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
                .map(Shipment::from)
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
                .map(Shipment::from);
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
                .map(Shipment::from)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));
    }

    private boolean writeModelExists(final ShipmentId shipmentId) {
        return shipmentId != null && writeRepository.createCriteria(ShipmentEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .isPresent();
    }
}
