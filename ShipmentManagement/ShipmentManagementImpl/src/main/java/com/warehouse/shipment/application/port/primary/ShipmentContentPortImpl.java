package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentContentRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.exception.ShipmentNotFoundException;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.domain.model.ShipmentNote;
import com.warehouse.exceptionhandler.exception.RestException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ShipmentContentPortImpl implements ShipmentContentPort {

    private static final int MAX_DOCUMENT_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf", "image/jpeg", "image/png", "text/plain",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ShipmentRepository shipmentRepository;
    private final ShipmentContentRepository contentRepository;
    private final ShipmentActorServicePort actorServicePort;

    public ShipmentContentPortImpl(final ShipmentRepository shipmentRepository,
                                   final ShipmentContentRepository contentRepository,
                                   final ShipmentActorServicePort actorServicePort) {
        this.shipmentRepository = shipmentRepository;
        this.contentRepository = contentRepository;
        this.actorServicePort = actorServicePort;
    }

    @Override
    @Transactional
    public ShipmentNote addNote(final ShipmentId shipmentId, final String body, final boolean internal) {
        requireShipment(shipmentId);
        final ShipmentNote note = new ShipmentNote(UUID.randomUUID(), shipmentId, body == null ? null : body.trim(), internal,
                actorServicePort.currentUserId(), Instant.now());
        contentRepository.saveNote(note);
        return note;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentNote> listNotes(final ShipmentId shipmentId) {
        requireShipment(shipmentId);
        return contentRepository.listNotes(shipmentId);
    }

    @Override
    @Transactional
    public ShipmentDocument addDocument(final ShipmentId shipmentId, final String filename,
                                        final String contentType, final byte[] content) {
        requireShipment(shipmentId);
        if (content == null || content.length == 0 || content.length > MAX_DOCUMENT_SIZE) {
            throw new IllegalArgumentException("Document must contain 1 byte to 5 MB");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Document type is not supported");
        }
        final String safeFilename = normalizeFilename(filename);
        final ShipmentDocument document = new ShipmentDocument(UUID.randomUUID(), shipmentId, safeFilename,
                contentType, content.length, actorServicePort.currentUserId(), Instant.now());
        contentRepository.saveDocument(new ShipmentDocumentFile(document, content));
        return document;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentDocument> listDocuments(final ShipmentId shipmentId) {
        requireShipment(shipmentId);
        return contentRepository.listDocuments(shipmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentDocumentFile getDocument(final ShipmentId shipmentId, final UUID documentId) {
        requireShipment(shipmentId);
        return contentRepository.findDocument(shipmentId, documentId)
                .orElseThrow(() -> new RestException(404, "Document was not found"));
    }

    private void requireShipment(final ShipmentId shipmentId) {
        if (!shipmentRepository.exists(shipmentId)) {
            throw new ShipmentNotFoundException(404, "Shipment was not found");
        }
    }

    private String normalizeFilename(final String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Document filename is required");
        }
        final String normalized = filename.replace('\\', '/');
        final String basename = normalized.substring(normalized.lastIndexOf('/') + 1).trim();
        if (basename.isBlank() || basename.equals(".") || basename.equals("..")
                || basename.length() > 255 || basename.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Document filename is invalid");
        }
        return basename;
    }
}
