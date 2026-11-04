package com.warehouse.incident.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.incident.domain.vo.IncidentShipment;

public interface IncidentShipmentServicePort {

    IncidentShipment getShipment(final ShipmentId shipmentId);
}
