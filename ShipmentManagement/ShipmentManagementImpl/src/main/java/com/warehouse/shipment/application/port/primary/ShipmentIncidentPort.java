package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentNoteCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentResolutionCommand;

public interface ShipmentIncidentPort {

    void reportIncident(final ShipmentIncidentNoteCommand command);

    void commentIncident(final ShipmentIncidentNoteCommand command);

    void resolveIncident(final ShipmentIncidentResolutionCommand command);

    void reopenIncident(final ShipmentIncidentNoteCommand command);
}
