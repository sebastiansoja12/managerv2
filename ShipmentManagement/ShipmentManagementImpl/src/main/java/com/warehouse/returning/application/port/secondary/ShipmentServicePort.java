package com.warehouse.returning.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.returning.domain.model.ReturnableShipment;

public interface ShipmentServicePort {

    ReturnableShipment getShipment(final ShipmentId shipmentId);

    ReturnableShipment getShipment(final TrackingNumber trackingNumber);

    void markReturned(final ShipmentId shipmentId);

    void markReturned(final ShipmentId shipmentId, final ShipmentId returnShipmentId);

    ShipmentId createReturnShipmentDraft(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void completeReturn(final ShipmentId shipmentId);

    void markDelivered(final ShipmentId shipmentId);

    void cancelReturnShipment(final ShipmentId shipmentId);
}
