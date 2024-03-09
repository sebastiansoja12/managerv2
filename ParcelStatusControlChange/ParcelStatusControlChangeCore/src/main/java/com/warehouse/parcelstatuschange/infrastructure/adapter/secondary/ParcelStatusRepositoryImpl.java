package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.parcelstatuschange.domain.port.secondary.ParcelStatusRepository;
import com.warehouse.parcelstatuschange.domain.vo.Parcel;
import com.warehouse.parcelstatuschange.domain.vo.Status;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.mapper.StatusMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelStatusRepositoryImpl implements ParcelStatusRepository {
    
    private final ParcelStatusReadRepository parcelStatusReadRepository;
    
    private final StatusMapper statusMapper = getMapper(StatusMapper.class);
    
    private final String exceptionMessage = "Parcel %s to update was not found";

    @Override
    public void update(Parcel parcel) {
		parcelStatusReadRepository.findById(parcel.getId())
				.orElseThrow(() -> new ParcelNotFoundException(404, String.format(exceptionMessage, parcel.getId())));
		final ParcelEntity parcelEntity = statusMapper.map(parcel);
		parcelEntity.shouldBeLocked();
		parcelStatusReadRepository.save(parcelEntity);
    }

    @Override
    public Status get(Long parcelId) {
        return parcelStatusReadRepository.findStatusById(parcelId)
                .map(statusMapper::mapToStatus)
                .orElseThrow(() -> new ParcelNotFoundException(404, String.format(exceptionMessage, parcelId)));
    }
}
