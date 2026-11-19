package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.domain.vo.ShipmentNoteIncidentSource;
import java.time.Instant;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.application.port.primary.result.ShipmentNoteResult;

import java.util.List;
import java.util.UUID;

public interface ShipmentContentPort {

    ShipmentNoteResult addNote(final ShipmentId shipmentId, final String body, final boolean internal);

    void addIncidentNote(final ShipmentId shipmentId, final ShipmentNoteIncidentSource incidentSource,
                         final String body, final UserId authorUserId, final Instant occurredAt);

    List<ShipmentNoteResult> listNotes(final ShipmentId shipmentId);

    ShipmentDocument addDocument(final ShipmentId shipmentId, final String filename,
                                 final String contentType, final byte[] content);

    List<ShipmentDocument> listDocuments(final ShipmentId shipmentId);

    ShipmentDocumentFile getDocument(final ShipmentId shipmentId, final UUID documentId);
}
