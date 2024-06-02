package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.ParcelStatus;
import com.warehouse.deliverymissed.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.ParcelIdDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.StatusDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.UpdateStatusParcelRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface UpdateStatusParcelRequestMapper {
    default UpdateStatusParcelRequestDto map(UpdateStatusParcelRequest updateStatusParcelRequest) {
        return UpdateStatusParcelRequestDto.builder()
                .parcel(ParcelDto.builder()
                        .parcelId(ParcelIdDto.builder()
                                .value(updateStatusParcelRequest.getParcelId())
                                .build())
                        .parcelStatus(map(updateStatusParcelRequest.getParcelStatus()))
                        .build())
                .build();
    }

    StatusDto map(ParcelStatus parcelStatus);
}
