package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.commonassets.helper.SnapshotDifference;
import com.warehouse.logisticsdelivery.application.port.secondary.AuditLogServicePort;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryAuditLogType;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAuditLog;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class DeliveryAuditLogListener {

    private static final Set<String> IGNORED_FIELDS = Set.of("deliveryId", "target", "shipmentId", "type",
            "createdAt", "token", "deliverySteps");

    private final AuditLogServicePort auditLogServicePort;

    public DeliveryAuditLogListener(final AuditLogServicePort auditLogServicePort) {
        this.auditLogServicePort = auditLogServicePort;
    }

    @EventListener
    public void handle(final DeliveryChanged event) {
        final DeliverySnapshot snapshot = event.getSnapshot();
        this.auditLogServicePort.record(new DeliveryAuditLog(snapshot.deliveryId(), auditLogType(event),
                SnapshotDifference.of(snapshot, IGNORED_FIELDS)));
    }

    private DeliveryAuditLogType auditLogType(final DeliveryChanged event) {
        return switch (event) {
            case final DeliveryCreated ignored -> DeliveryAuditLogType.CREATED;
            case final DeliveryAttemptRecorded ignored -> DeliveryAuditLogType.ATTEMPT_RECORDED;
            case final DeliveryCompleted ignored -> DeliveryAuditLogType.COMPLETED;
            case final DeliveryCanceled ignored -> DeliveryAuditLogType.CANCELED;
            default -> DeliveryAuditLogType.MODIFIED;
        };
    }
}
