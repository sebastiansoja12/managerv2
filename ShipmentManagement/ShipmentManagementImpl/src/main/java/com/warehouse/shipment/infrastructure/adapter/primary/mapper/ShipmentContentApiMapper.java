package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentNote;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentDocumentResponseApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentNoteResponseApi;

public class ShipmentContentApiMapper {

    public ShipmentNoteResponseApi toResponse(final ShipmentNote note) {
        return new ShipmentNoteResponseApi(note.noteId(), note.shipmentId().getValue(), note.body(),
                note.internal(), note.authorUserId().value(), note.createdAt());
    }

    public ShipmentDocumentResponseApi toResponse(final ShipmentDocument document) {
        return new ShipmentDocumentResponseApi(document.documentId(), document.shipmentId().getValue(),
                document.filename(), document.contentType(), document.sizeBytes(),
                document.uploadedByUserId().value(), document.createdAt());
    }
}
