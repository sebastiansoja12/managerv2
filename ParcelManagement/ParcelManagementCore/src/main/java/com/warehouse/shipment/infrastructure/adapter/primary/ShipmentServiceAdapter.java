package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.domain.model.ShipmentRequest;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.domain.model.UpdateParcelRequest;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.shipment.infrastructure.api.dto.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentServiceAdapter implements ShipmentService {

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

    private final ShipmentPort shipmentPort;

    @Override
    public ShipmentResponseDto ship(ShipmentRequestDto requestDto) {
        final ShipmentRequest request = requestMapper.map(requestDto);
        final ShipmentResponse response = shipmentPort.ship(request);
        return responseMapper.map(response);
    }

    @Override
    public UpdateParcelResponseDto update(UpdateParcelRequestDto parcelRequest) {
        final UpdateParcelRequest updateParcelRequest = requestMapper.map(parcelRequest);
        final UpdateParcelResponse updateParcelResponse = shipmentPort.update(updateParcelRequest);
        return responseMapper.map(updateParcelResponse);
    }
}
