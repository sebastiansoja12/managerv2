package com.warehouse.shipment.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEntityMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentModelMapper;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentEntityMapper shipmentEntityMapper = getMapper(ShipmentEntityMapper.class);

    private final ShipmentModelMapper shipmentModelMapper = getMapper(ShipmentModelMapper.class);

    private final ShipmentReadRepository repository;

    @Override
    public void createOrUpdate(final Shipment shipment) {
        final ShipmentEntity entity = shipmentEntityMapper.map(shipment);
        repository.save(entity);
    }

    @Override
    public Shipment findById(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return null;
        }
        return repository.findByShipmentId(shipmentId)
                .map(shipmentModelMapper::map)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment was not found"));
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return false;
        }
        return repository.existsById(shipmentId);
    }
}
