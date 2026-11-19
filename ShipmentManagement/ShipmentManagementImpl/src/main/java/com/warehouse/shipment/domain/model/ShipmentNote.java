package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import com.warehouse.shipment.domain.vo.ShipmentNoteIncidentSource;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

public record ShipmentNote(UUID noteId, ShipmentId shipmentId, String body, boolean internal,
                           UserId authorUserId, Instant createdAt, ShipmentNoteIncidentSource incidentSource) {

    public ShipmentNote {
        final boolean bodyMissing = body == null || body.isBlank();
        if ((bodyMissing && incidentSource == null) || (body != null && body.length() > 4000)) {
            throw new IllegalArgumentException("Note must contain 1 to 4000 characters");
        }
    }

    public static ShipmentNote fromIncident(final ShipmentId shipmentId, final ShipmentNoteIncidentSource incidentSource,
                                            final String body, final UserId authorUserId, final Instant createdAt) {
        final UUID noteId = UUID.nameUUIDFromBytes(("incident-note:" + incidentSource.incidentId().value() + ":"
                + incidentSource.type() + ":" + createdAt).getBytes(StandardCharsets.UTF_8));
        return new ShipmentNote(noteId, shipmentId, body == null || body.isBlank() ? null : body.strip(), true,
                authorUserId, createdAt, incidentSource);
    }
}
