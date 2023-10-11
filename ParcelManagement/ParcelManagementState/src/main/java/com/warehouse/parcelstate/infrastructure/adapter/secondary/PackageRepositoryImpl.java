package com.warehouse.parcelstate.infrastructure.adapter.secondary;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.domain.port.secondary.ParcelStateRepository;

import com.warehouse.parcelstate.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.parcelstate.infrastructure.adapter.secondary.mapper.ParcelMapper;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PackageRepositoryImpl implements ParcelStateRepository {

    private final PackageReadRepository repository;

    private final ParcelMapper parcelMapper;

    @Override
    public Long save(Parcel parcel) {
        return null;
    }

    @Override
    public void delete(Long parcelId) {

    }

    @Override
    public Parcel loadParcelById(Long parcelId) {
        return null;
    }

    @Override
    public Parcel update(Parcel parcelUpdate) {
        final ParcelEntity parcel = parcelMapper.map(parcelUpdate);
        final ParcelEntity updatedEntity = repository.save(parcel);
        return parcelMapper.map(updatedEntity);
    }
}
