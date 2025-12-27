package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.ShipmentReturnedCommand;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.util.Map;

public interface ReturningServicePort {
    void notifyShipmentReturn(final ShipmentSnapshot snapshot);
    Map<ShipmentId, ReturnId> shipmentReturnCommand(final ShipmentReturnedCommand shipmentReturnedCommand);
    void notifyShipmentUpdated(final ShipmentSnapshot snapshot);
    void notifyShipmentReturnCompleted(final ShipmentSnapshot snapshot);
}
