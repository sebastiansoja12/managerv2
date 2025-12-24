package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.*;

public interface RouteLogServicePort {
    RouteProcess notifyShipmentCreated(final ShipmentId shipmentId, final SoftwareConfiguration softwareConfiguration);
    RouteProcess notifyRecipientChanged(final ShipmentId shipmentId, final Recipient recipient, final SoftwareConfiguration softwareConfiguration);
    RouteProcess notifyPersonChanged(final ShipmentId shipmentId, final Person person, final SoftwareConfiguration softwareConfiguration);
    void notifyShipmentUpdated(final ShipmentSnapshot snapshot);
}
