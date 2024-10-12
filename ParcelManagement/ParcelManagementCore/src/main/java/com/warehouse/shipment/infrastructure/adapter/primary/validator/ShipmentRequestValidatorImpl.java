package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

import java.util.Objects;

// TODO later
public class ShipmentRequestValidatorImpl implements ShipmentRequestValidator {

    @Override
    public void validateBody(final ShipmentRequestDto shipmentRequest) {
        validateRequest(shipmentRequest);
    }

    @Override
    public void validateBody(final ShipmentUpdateRequestDto shipmentRequest) {
        validateRequest(shipmentRequest);
    }

    @Override
    public void validateBody(final ShipmentIdDto shipmentId) {
        validateRequest(shipmentId);
        validateValue(shipmentId);
    }

    private void validateRequest(final Object obj) {
        if (Objects.isNull(obj)) {
            throw new EmptyRequestException("Request cannot be null");
        }
    }

    private void validateValue(final ShipmentIdDto shipmentId) {
        if (Objects.isNull(shipmentId.getValue())) {
            throw new EmptyRequestException("Value cannot be null");
        }
    }
}
