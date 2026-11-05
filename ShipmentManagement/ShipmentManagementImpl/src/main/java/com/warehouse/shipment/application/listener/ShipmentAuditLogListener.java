package com.warehouse.shipment.application.listener;

import com.warehouse.shipment.application.port.secondary.AuditLogServicePort;
import com.warehouse.shipment.domain.enumeration.ShipmentAuditLogType;
import com.warehouse.shipment.domain.event.ShipmentCanceled;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import com.warehouse.shipment.domain.event.ShipmentCollected;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentDocumentAdded;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentNoteAdded;
import com.warehouse.shipment.domain.event.ShipmentRecipientChanged;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentRerouted;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.event.ShipmentReturnedCompleted;
import com.warehouse.shipment.domain.event.ShipmentSenderChanged;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;
import com.warehouse.shipment.domain.event.ShipmentTypeChanged;
import com.warehouse.shipment.domain.service.ShipmentChangeDetector;
import com.warehouse.shipment.domain.vo.ShipmentAuditLog;
import com.warehouse.shipment.domain.vo.ShipmentFieldChange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentAuditLogListener {

    private final AuditLogServicePort auditLogServicePort;

    private final ShipmentChangeDetector shipmentChangeDetector;

    public ShipmentAuditLogListener(final AuditLogServicePort auditLogServicePort,
                                    final ShipmentChangeDetector shipmentChangeDetector) {
        this.auditLogServicePort = auditLogServicePort;
        this.shipmentChangeDetector = shipmentChangeDetector;
    }

    @EventListener
    public void handle(final ShipmentChanged event) {
        auditLogType(event).ifPresent(type -> this.auditLogServicePort.record(
                new ShipmentAuditLog(event.shipmentId(), type, changes(event))));
    }

    @EventListener
    public void handle(final ShipmentNoteAdded event) {
        this.auditLogServicePort.record(new ShipmentAuditLog(event.getShipmentId(), ShipmentAuditLogType.NOTE_ADDED,
                List.of(new ShipmentFieldChange("note.internal", null, String.valueOf(event.isInternal())))));
    }

    @EventListener
    public void handle(final ShipmentDocumentAdded event) {
        this.auditLogServicePort.record(new ShipmentAuditLog(event.getShipmentId(),
                ShipmentAuditLogType.DOCUMENT_ADDED,
                List.of(new ShipmentFieldChange("document.filename", null, event.getFilename()))));
    }

    private Optional<ShipmentAuditLogType> auditLogType(final ShipmentChanged event) {
        return Optional.ofNullable(switch (event) {
            case final ShipmentCreated ignored -> ShipmentAuditLogType.CREATED;
            case final ShipmentSenderChanged ignored -> ShipmentAuditLogType.SENDER_CHANGED;
            case final ShipmentRecipientChanged ignored -> ShipmentAuditLogType.RECIPIENT_CHANGED;
            case final ShipmentTypeChanged ignored -> ShipmentAuditLogType.TYPE_CHANGED;
            case final ShipmentRerouted ignored -> ShipmentAuditLogType.REROUTED;
            case final ShipmentCanceled ignored -> ShipmentAuditLogType.CANCELED;
            case final ShipmentLocked ignored -> ShipmentAuditLogType.LOCKED;
            case final ShipmentSent ignored -> ShipmentAuditLogType.SENT;
            case final ShipmentCollected ignored -> ShipmentAuditLogType.COLLECTED;
            case final ShipmentDelivered ignored -> ShipmentAuditLogType.DELIVERED;
            case final ShipmentRedirected ignored -> ShipmentAuditLogType.REDIRECTED;
            case final ShipmentReturned ignored -> ShipmentAuditLogType.RETURNED;
            case final ShipmentReturnedCompleted ignored -> ShipmentAuditLogType.RETURN_COMPLETED;
            case final ShipmentStatusChanged statusChanged when isPlainStatusChange(statusChanged) ->
                    ShipmentAuditLogType.STATUS_CHANGED;
            default -> null;
        });
    }

    private boolean isPlainStatusChange(final ShipmentStatusChanged event) {
        return event.getClass() == ShipmentStatusChanged.class;
    }

    private List<ShipmentFieldChange> changes(final ShipmentChanged event) {
        return event.getPreviousSnapshot()
                .map(previousSnapshot -> this.shipmentChangeDetector.detect(previousSnapshot, event.getSnapshot()))
                .orElseGet(() -> event instanceof ShipmentStatusChanged
                        ? this.shipmentChangeDetector.currentStatus(event.getSnapshot())
                        : List.of());
    }
}
