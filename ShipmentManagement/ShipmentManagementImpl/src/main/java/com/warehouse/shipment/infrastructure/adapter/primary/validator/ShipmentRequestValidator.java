package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

public interface ShipmentRequestValidator {
    void validateRequest(final ShipmentCreateRequestApi shipmentRequest, final ShipmentValidationRules validationRules);
    void validateBody(final ShipmentUpdateRequestApi shipmentRequest);
    void validateBody(final ShipmentIdDto parcelId);
    void validateBody(final ShipmentStatusRequestApi shipmentStatusRequest);
    void validateBody(final SignatureChangeRequestApi signatureChangeRequest);
}
