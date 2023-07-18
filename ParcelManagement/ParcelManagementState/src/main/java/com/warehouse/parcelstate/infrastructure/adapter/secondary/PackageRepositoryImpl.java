package com.warehouse.parcelstate.infrastructure.adapter.secondary;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.domain.port.secondary.ParcelStateRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PackageRepositoryImpl implements ParcelStateRepository {

    private final PackageReadRepository repository;

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
        return null;
    }
}
