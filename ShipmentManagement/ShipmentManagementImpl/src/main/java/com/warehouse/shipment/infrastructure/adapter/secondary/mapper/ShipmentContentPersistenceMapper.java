package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.document.api.DocumentFile;
import com.warehouse.document.api.DocumentMetadata;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.domain.model.ShipmentNote;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentNoteEntity;

public class ShipmentContentPersistenceMapper {

    public ShipmentNoteEntity toEntity(final ShipmentNote note, final OperatorId operatorId) {
        return new ShipmentNoteEntity(note.noteId(), note.shipmentId(), operatorId,
                note.body(), note.internal(), note.authorUserId(), note.createdAt());
    }

    public ShipmentNote toDomain(final ShipmentNoteEntity entity) {
        return new ShipmentNote(entity.getNoteId(), entity.getShipmentId(),
                entity.getBody(), entity.isInternal(), entity.getAuthorUserId(),
                entity.getCreatedAt());
    }

    public DocumentFile toDocumentFile(final ShipmentDocumentFile documentFile) {
        final ShipmentDocument document = documentFile.document();
        return new DocumentFile(new DocumentMetadata(document.documentId(), document.filename(),
                document.contentType(), document.sizeBytes(), document.uploadedByUserId(),
                document.createdAt()), documentFile.content());
    }

    public ShipmentDocument toDomain(final DocumentMetadata metadata, final ShipmentId shipmentId) {
        return new ShipmentDocument(metadata.documentId(), shipmentId, metadata.filename(),
                metadata.contentType(), metadata.sizeBytes(), metadata.uploadedByUserId(),
                metadata.createdAt());
    }

    public ShipmentDocumentFile toDomain(final DocumentFile documentFile, final ShipmentId shipmentId) {
        return new ShipmentDocumentFile(toDomain(documentFile.metadata(), shipmentId), documentFile.content());
    }
}
