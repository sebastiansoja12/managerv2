package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.infrastructure.api.dto.ParcelIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

import java.util.Objects;

public class ShipmentRequestValidatorImpl implements ShipmentRequestValidator {

    @Override
    public void validateBody(final ShipmentRequestDto shipmentRequest) {

    }

    @Override
    public void validateBody(final ShipmentUpdateRequestDto shipmentRequest) {
        validateRequest(shipmentRequest);
    }

    @Override
    public void validateBody(final ParcelIdDto parcelId) {

    }

    private void validateRequest(final ShipmentUpdateRequestDto shipmentRequest) {
        if (Objects.isNull(shipmentRequest)) {
            throw new RuntimeException("Shipment request cannot be null");
        }
    }
}
