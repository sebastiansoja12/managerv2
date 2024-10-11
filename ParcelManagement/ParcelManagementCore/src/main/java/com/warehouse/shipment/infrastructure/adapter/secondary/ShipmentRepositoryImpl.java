package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentReadRepository repository;

    private final ParcelMapper parcelMapper;

    @Override
    public void save(final Shipment shipment) {
        final ParcelEntity entity = parcelMapper.map(shipment);
        repository.save(entity);
    }

    @Override
    public void update(final Shipment shipment) {
        final ParcelEntity entity = parcelMapper.map(shipment);
        repository.save(entity);
    }

    @Override
    public Shipment findById(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return null;
        }
        return repository.findShipmentById(shipmentId.getValue()).map(parcelMapper::mapToShipment).orElseThrow(
                () -> new ParcelNotFoundException("Shipment was not found"));
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return false;
        }
        return repository.existsById(shipmentId.getValue());
    }

    @Override
    public ShipmentId nextShipmentId() {
        return new ShipmentId(repository.nextShipmentId());
    }
}
