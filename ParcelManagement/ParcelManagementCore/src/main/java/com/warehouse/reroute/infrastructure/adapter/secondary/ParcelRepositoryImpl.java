package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.port.secondary.ParcelRepository;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelRepositoryImpl implements ParcelRepository {

    private final ParcelMapper mapper;

    private final ParcelShipmentReadRepository parcelShipmentReadRepository;

    @Override
    public ParcelUpdateResponse updateParcel(Parcel parcel, City city) {
        final ParcelEntity parcelEntity = mapper.map(parcel);
        parcelEntity.setDestination(city.getName());
        parcelShipmentReadRepository.save(parcelEntity);
        return mapper.mapFromParcelEntityToParcelResponse(parcelEntity);
    }

}
