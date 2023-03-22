package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentReadRepository repository;

    private final ParcelMapper parcelMapper;

    @Override
    public Long save(Parcel parcel) {

        final ParcelEntity entity = parcelMapper.map(parcel);

        repository.save(entity);

        return entity.getId();
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

    @Override
    public UpdateParcelResponse update(Parcel parcelUpdate) {

        final ParcelEntity entity = parcelMapper.mapForUpdate(parcelUpdate);

        repository.save(entity);

        return parcelMapper.mapToUpdateParcelResponse(entity);
    }
}
