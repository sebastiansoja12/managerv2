package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentUpdateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.SignatureChangeRequestApi;

public interface ShipmentRequestValidator {
    void validateBody(final ShipmentCreateRequestApi shipmentRequest);
    void validateBody(final ShipmentUpdateRequestApi shipmentRequest);
    void validateBody(final ShipmentIdDto parcelId);
    void validateBody(final ShipmentStatusRequestApi shipmentStatusRequest);
    void validateBody(final SignatureChangeRequestApi signatureChangeRequest);
}
