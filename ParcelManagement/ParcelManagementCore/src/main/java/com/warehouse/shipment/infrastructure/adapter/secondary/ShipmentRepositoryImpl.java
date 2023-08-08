package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;
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
    public Parcel save(ShipmentParcel parcel) {

        final ParcelEntity entity = parcelMapper.map(parcel);

        repository.save(entity);

        return parcelMapper.map(entity);
    }

    @Override
    public Parcel update(ParcelUpdate parcel) {

        final ParcelEntity entity = parcelMapper.map(parcel);

        repository.save(entity);

        return parcelMapper.map(entity);
    }

    @Override
    public void delete(Long parcelId) {
        repository.deleteById(parcelId);
    }

    @Override
    public Parcel loadParcelById(Long parcelId) {
        return repository.findParcelEntityById(parcelId).map(parcelMapper::map).orElseThrow(
                () -> new ParcelNotFoundException("Parcel was not found"));
    }
}
