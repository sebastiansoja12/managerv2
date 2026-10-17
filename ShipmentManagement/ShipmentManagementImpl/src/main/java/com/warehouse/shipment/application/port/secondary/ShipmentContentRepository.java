package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.domain.model.ShipmentNote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentContentRepository {

    void saveNote(final ShipmentNote note);

    List<ShipmentNote> listNotes(final ShipmentId shipmentId);

    void saveDocument(final ShipmentDocumentFile documentFile);

    List<ShipmentDocument> listDocuments(final ShipmentId shipmentId);

    Optional<ShipmentDocumentFile> findDocument(final ShipmentId shipmentId, final UUID documentId);
}
