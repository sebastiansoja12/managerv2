package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.exception.ParcelNotFoundException;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.port.secondary.ParcelRepository;
import com.warehouse.reroute.domain.vo.ParcelResponse;
import com.warehouse.reroute.infrastructure.adapter.entity.ParcelEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ParcelRepositoryImpl implements ParcelRepository {

    private final ParcelMapper mapper;

    private final ParcelShipmentReadRepository parcelShipmentReadRepository;

    @Override
    public Optional<ParcelResponse> update(UpdateParcelRequest parcelRequest) {

        final ParcelEntity parcelEntity = mapper.mapToParcelEntity(parcelRequest);

        parcelShipmentReadRepository.save(parcelEntity);

        return Optional.ofNullable(mapper.mapFromParcelEntityToParcelResponse(parcelEntity));
    }

    @Override
    public ParcelResponse loadByParcelId(Long parcelId) {

        final Optional<ParcelEntity> parcelEntity = parcelShipmentReadRepository.loadByParcelId(parcelId);

        return parcelEntity.map(mapper::mapFromParcelEntityToParcelResponse).orElseThrow(
                () -> new ParcelNotFoundException("Parcel was not found")
        );

    }

}
