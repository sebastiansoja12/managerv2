package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.exception.ParcelNotFoundException;
import com.warehouse.reroute.domain.port.secondary.ParcelRepository;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ParcelRepositoryImpl implements ParcelRepository {

    private final ParcelMapper mapper;

    private final ParcelShipmentReadRepository parcelShipmentReadRepository;

    @Override
    public ParcelUpdateResponse loadByParcelId(Long parcelId) {

        final Optional<ParcelEntity> parcelEntity = parcelShipmentReadRepository.loadByParcelId(parcelId);

        return parcelEntity.map(mapper::mapFromParcelEntityToParcelResponse).orElseThrow(
                () -> new ParcelNotFoundException("Parcel was not found")
        );

    }

}
