package com.warehouse.incident.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.incident.domain.model.Incident;
import com.warehouse.incident.domain.vo.IncidentRepackaging;
import com.warehouse.incident.domain.vo.IncidentShipment;

public interface IncidentShipmentServicePort {

    IncidentShipment getShipment(final ShipmentId shipmentId);

    void notifyReported(final Incident incident);

    void notifyCommented(final Incident incident);

    void notifyResolved(final Incident incident, final IncidentRepackaging repackaging);

    void notifyReopened(final Incident incident);
}
