package com.warehouse.shipment.infrastructure.api;

import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;

public interface ShipmentService {
    UpdateParcelResponseDto update(UpdateParcelRequestDto parcelRequest);
}
