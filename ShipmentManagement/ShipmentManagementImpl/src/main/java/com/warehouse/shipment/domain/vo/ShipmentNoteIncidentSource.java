package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.shipment.domain.enumeration.IncidentNoteType;

import java.util.Objects;

public record ShipmentNoteIncidentSource(IncidentId incidentId, IncidentNoteType type, String reason) {

    public ShipmentNoteIncidentSource {
        Objects.requireNonNull(incidentId, "Incident ID cannot be null");
        Objects.requireNonNull(type, "Incident note type cannot be null");
    }
}
