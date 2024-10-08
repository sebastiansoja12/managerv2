package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentReadRepository repository;

    private final ParcelMapper parcelMapper;

    @Override
    public Parcel save(final Shipment parcel) {

        final ParcelEntity entity = parcelMapper.map(parcel);

        repository.save(entity);

        return parcelMapper.map(entity);
    }

    @Override
    public Parcel update(ShipmentUpdate parcel) {

        final ParcelEntity entity = parcelMapper.map(parcel);

        repository.save(entity);

        return parcelMapper.map(entity);
    }

    @Override
    public Parcel findParcelById(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return null;
        }
        return repository.findParcelEntityById(shipmentId.getId()).map(parcelMapper::map).orElseThrow(
                () -> new ParcelNotFoundException("Parcel was not found"));
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return false;
        }
        return repository.existsById(shipmentId.getId());
    }
}
