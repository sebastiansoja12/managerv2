package com.warehouse.returning.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.model.ReturnableShipment;

public interface ShipmentServicePort {

    ReturnableShipment getShipment(final ShipmentId shipmentId);

    void markReturned(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void completeReturn(final ShipmentId shipmentId);
    void cancelReturnShipment(final ShipmentId shipmentId);
}
