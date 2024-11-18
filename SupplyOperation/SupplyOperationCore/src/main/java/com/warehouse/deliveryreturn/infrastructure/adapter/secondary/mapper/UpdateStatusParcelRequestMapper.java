package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.vo.ParcelStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentIdDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentStatusDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusParcelRequestDto;

@Mapper
public interface UpdateStatusParcelRequestMapper {
    default UpdateStatusParcelRequestDto map(final UpdateStatusParcelRequest request) {
        final ShipmentIdDto shipmentId = ShipmentIdDto.builder().value(request.getParcelId()).build();
        return new UpdateStatusParcelRequestDto(shipmentId, ShipmentStatusDto.RETURN);
    }

    ShipmentStatusDto map(ParcelStatus parcelStatus);
}
