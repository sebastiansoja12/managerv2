package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

public interface ShipmentRequestValidator {
    void validateBody(final ShipmentRequestDto shipmentRequest);
    void validateBody(final ShipmentUpdateRequestDto shipmentRequest);
    void validateBody(final ShipmentIdDto parcelId);
    void validateBody(final ShipmentStatusRequestDto shipmentStatusRequest);
}
