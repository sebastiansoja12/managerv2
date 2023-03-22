package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.port.secondary.ParcelPort;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelAdapter implements ParcelPort {
    private final ShipmentService shipmentService;

    private final ParcelMapper parcelMapper;

    @Override
    public ParcelUpdateResponse update(UpdateParcelRequest request) {
        final UpdateParcelRequestDto updateParcelRequest = parcelMapper.map(request);
        final UpdateParcelResponseDto updateParcelResponse = shipmentService.update(updateParcelRequest);
        return parcelMapper.map(updateParcelResponse);
    }

}
