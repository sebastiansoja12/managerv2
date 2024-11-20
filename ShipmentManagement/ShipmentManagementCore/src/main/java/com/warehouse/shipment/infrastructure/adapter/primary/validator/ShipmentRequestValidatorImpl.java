package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import java.util.Objects;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusRequestDto;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.SignatureChangeRequestDto;

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

    @Override
    public void validateBody(final ShipmentStatusRequestDto shipmentStatusRequest) {

    }

    @Override
    public void validateBody(final SignatureChangeRequestDto signatureChangeRequest) {

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
