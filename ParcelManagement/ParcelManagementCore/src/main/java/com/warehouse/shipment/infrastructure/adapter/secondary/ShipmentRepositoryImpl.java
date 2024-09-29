package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;
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
    public Parcel save(ShipmentParcel parcel) {

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
    public Parcel findParcelById(final ParcelId parcelId) {
        if (parcelId == null) {
            return null;
        }
        return repository.findParcelEntityById(parcelId.getId()).map(parcelMapper::map).orElseThrow(
                () -> new ParcelNotFoundException("Parcel was not found"));
    }

    @Override
    public boolean exists(Long parcelId) {
        return repository.existsById(parcelId);
    }
}
