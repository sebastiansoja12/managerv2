package com.warehouse.routetracker.infrastructure.adapter.secondary;


import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.port.secondary.ParcelStatusUpdateRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.StatusMapper;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class ParcelStatusUpdateRepositoryImpl implements ParcelStatusUpdateRepository {

    private final ParcelReadRepository parcelReadRepository;

    private final StatusMapper mapper = getMapper(StatusMapper.class);

    private final String ERROR_MESSAGE = "Parcel to update with id '%s' was not found";


    @Override
    public void updateStatus(Long parcelId, Status status) {
        final ParcelEntity parcelEntity = parcelReadRepository
                .findById(parcelId)
                .map(parcel -> update(status, parcel))
                .orElseThrow(() -> new ParcelNotFoundException(String.format(ERROR_MESSAGE, parcelId)));

        parcelReadRepository.save(parcelEntity);
    }

    private ParcelEntity update(Status status, ParcelEntity parcel) {
        parcel.updateStatus(mapper.map(status));
        parcel.setUpdatedAt(LocalDateTime.now());
        return parcel;
    }
}
