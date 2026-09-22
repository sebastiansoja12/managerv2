package com.warehouse.shipment.infrastructure;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;

public interface ShipmentApiService {
    ShipmentDetailsDto getShipment(final ShipmentId shipmentId);

    ShipmentRejectResponseDto rejectShipment(final ShipmentRejectRequestDto shipmentRejectRequest);

    void markReturned(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void completeReturn(final ShipmentId shipmentId);
    void cancelReturnShipment(final ShipmentId shipmentId);
}
