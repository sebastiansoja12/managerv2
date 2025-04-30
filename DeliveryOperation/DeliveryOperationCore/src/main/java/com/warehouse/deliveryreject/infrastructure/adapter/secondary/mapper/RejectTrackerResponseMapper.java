package com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper;


import java.util.UUID;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.deliveryreject.domain.vo.RejectTrackerResponse;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.ProcessIdDto;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.RejectTrackerResponseDto;

@Mapper
public interface RejectTrackerResponseMapper {
    RejectTrackerResponse map(final RejectTrackerResponseDto rejectTrackerResponse);

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.value());
    }

    UUID map(final ProcessIdDto value);
}
