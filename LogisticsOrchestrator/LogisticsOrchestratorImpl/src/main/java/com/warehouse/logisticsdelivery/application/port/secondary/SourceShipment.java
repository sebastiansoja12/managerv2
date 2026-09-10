package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryDerivationType;

public record SourceShipment(ShipmentId shipmentId, DeliveryDerivationType derivationType) {
}
