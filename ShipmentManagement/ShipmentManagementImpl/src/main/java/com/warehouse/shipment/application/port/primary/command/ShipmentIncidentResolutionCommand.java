package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.shipment.domain.enumeration.IncidentShipmentAction;
import com.warehouse.shipment.domain.vo.ShipmentRepackaging;

public record ShipmentIncidentResolutionCommand(ShipmentIncidentNoteCommand note,
                                                IncidentShipmentAction action,
                                                ShipmentRepackaging repackaging) {
}
