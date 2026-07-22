package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;

public class ShipmentReadModelRepositoryImpl implements ShipmentReadModelRepository {

    private final OperatorFilteredRepository<ShipmentReadEntity> repository;

    public ShipmentReadModelRepositoryImpl(final OperatorFilteredRepository<ShipmentReadEntity> repository) {
        this.repository = repository;
    }

    @Override
    public void sync(final ShipmentSnapshot snapshot) {
        final ShipmentReadEntity entity = ShipmentReadEntity.from(snapshot);
        if (exists(snapshot.shipmentId())) {
            this.repository.update(entity);
        } else {
            this.repository.create(entity);
        }
    }

    @Override
    public Optional<Shipment> findById(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return Optional.empty();
        }
        return this.repository.createCriteria(ShipmentReadEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .map(Shipment::from);
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return shipmentId != null && this.repository.createCriteria(ShipmentReadEntity.class)
                .eq("shipmentId.value", shipmentId.getValue())
                .one()
                .isPresent();
    }

    @Override
    public Optional<Shipment> findByExternalId(final ExternalId<String> externalId) {
        return this.repository.createCriteria(ShipmentReadEntity.class)
                .eq("externalId.value", externalId.value())
                .one()
                .map(Shipment::from);
    }

    @Override
    public Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId) {
        return this.repository.createCriteria(ShipmentReadEntity.class)
                .eq("externalId.value", externalId.value())
                .one()
                .map(ShipmentReadEntity::getShipmentId);
    }

    @Override
    public Optional<Shipment> findByTrackingNumber(final TrackingNumber trackingNumber) {
        return this.repository.createCriteria(ShipmentReadEntity.class)
                .eq("trackingNumber.value", trackingNumber.value())
                .one()
                .map(Shipment::from);
    }
}
