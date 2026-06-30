package com.warehouse.shipment.infrastructure;

import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;

public interface ShipmentApiService {
    ShipmentRejectResponseDto rejectShipment(final ShipmentRejectRequestDto shipmentRejectRequest);
}
