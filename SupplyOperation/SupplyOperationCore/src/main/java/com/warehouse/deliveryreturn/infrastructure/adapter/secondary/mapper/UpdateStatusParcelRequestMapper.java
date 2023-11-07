package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.vo.ParcelStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ParcelIdDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.StatusDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusParcelRequestDto;

@Mapper
public interface UpdateStatusParcelRequestMapper {
    default UpdateStatusParcelRequestDto map(UpdateStatusParcelRequest updateStatusParcelRequest) {
        final ParcelIdDto parcelId = ParcelIdDto.builder()
                .value(updateStatusParcelRequest.getParcelId())
                .build();
        final StatusDto status = map(updateStatusParcelRequest.getParcelStatus());
        final ParcelDto parcel = new ParcelDto(parcelId, status);
        return new UpdateStatusParcelRequestDto(parcel);
    }

    StatusDto map(ParcelStatus parcelStatus);
}
