package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusRequestDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentCreateRequestDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentUpdateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.SignatureChangeRequestDto;

public interface ShipmentRequestValidator {
    void validateBody(final ShipmentCreateRequestDto shipmentRequest);
    void validateBody(final ShipmentUpdateRequestApi shipmentRequest);
    void validateBody(final ShipmentIdDto parcelId);
    void validateBody(final ShipmentStatusRequestDto shipmentStatusRequest);
    void validateBody(final SignatureChangeRequestDto signatureChangeRequest);
}
