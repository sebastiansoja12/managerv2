package com.warehouse.shipment.infrastructure.api;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentResponseDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;

public interface ShipmentService {
    ShipmentResponseDto ship(ShipmentRequestDto requestDto);

    UpdateParcelResponseDto update(UpdateParcelRequestDto parcelRequest);
}
