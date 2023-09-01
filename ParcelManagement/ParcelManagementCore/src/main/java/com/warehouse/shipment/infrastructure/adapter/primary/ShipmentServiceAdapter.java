package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.domain.model.UpdateParcelRequest;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePort;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentServiceAdapter implements ShipmentService {

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

    private final ShipmentReroutePort shipmentReroutePort;

    private final ShipmentPort shipmentPort;

    @Override
    public UpdateParcelResponseDto update(UpdateParcelRequestDto parcelRequest) {
        final UpdateParcelRequest updateParcelRequest = requestMapper.map(parcelRequest);
        final UpdateParcelResponse updateParcelResponse = shipmentReroutePort.reroute(updateParcelRequest);
        return responseMapper.map(updateParcelResponse);
    }

    @Override
    public boolean exists(Long parcelId) {
        return shipmentPort.exists(parcelId);
    }
}
