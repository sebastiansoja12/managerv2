package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary;

import com.warehouse.parcelstatuschange.domain.vo.Status;
import com.warehouse.parcelstatuschange.domain.vo.StatusRequest;
import com.warehouse.parcelstatuschange.domain.port.secondary.ParcelStatusRepository;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.mapper.StatusMapper;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class ParcelStatusRepositoryImpl implements ParcelStatusRepository {
    
    private final ParcelStatusReadRepository parcelStatusReadRepository;
    
    private final StatusMapper statusMapper = getMapper(StatusMapper.class);
    
    private final String exceptionMessage = "Parcel %s to update was not found";

    @Override
    public void update(StatusRequest statusRequest) {
		final ParcelEntity parcel = parcelStatusReadRepository.findById(statusRequest.getParcel().getId()).orElseThrow(
				() -> new ParcelNotFoundException(8070,
						String.format(exceptionMessage, statusRequest.getParcel().getId())));
		final Status status = statusRequest.getParcel().getParcelStatus();
		parcel.setStatus(statusMapper.map(status));
        parcel.setUpdatedAt(LocalDateTime.now());
		parcelStatusReadRepository.save(parcel);
    }
}
