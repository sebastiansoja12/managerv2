package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.domain.model.ShipmentNote;

import java.util.List;
import java.util.UUID;

public interface ShipmentContentPort {

    ShipmentNote addNote(final ShipmentId shipmentId, final String body, final boolean internal);

    List<ShipmentNote> listNotes(final ShipmentId shipmentId);

    ShipmentDocument addDocument(final ShipmentId shipmentId, final String filename,
                                 final String contentType, final byte[] content);

    List<ShipmentDocument> listDocuments(final ShipmentId shipmentId);

    ShipmentDocumentFile getDocument(final ShipmentId shipmentId, final UUID documentId);
}
