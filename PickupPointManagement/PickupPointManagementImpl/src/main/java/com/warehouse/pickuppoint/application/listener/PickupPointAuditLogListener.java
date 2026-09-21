package com.warehouse.pickuppoint.application.listener;

import com.warehouse.commonassets.helper.FieldDifference;
import com.warehouse.commonassets.helper.SnapshotDifference;
import com.warehouse.pickuppoint.application.port.secondary.AuditLogServicePort;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointAuditLogType;
import com.warehouse.pickuppoint.domain.event.PickupPointChanged;
import com.warehouse.pickuppoint.domain.event.PickupPointClosed;
import com.warehouse.pickuppoint.domain.event.PickupPointCreated;
import com.warehouse.pickuppoint.domain.event.PickupPointResumed;
import com.warehouse.pickuppoint.domain.event.PickupPointSuspended;
import com.warehouse.pickuppoint.domain.event.PickupPointUpdated;
import com.warehouse.pickuppoint.domain.vo.PickupPointAuditLog;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class PickupPointAuditLogListener {

    private static final Set<String> IGNORED_FIELDS = Set.of("version", "createdAt", "updatedAt");

    private final AuditLogServicePort auditLogServicePort;

    public PickupPointAuditLogListener(final AuditLogServicePort auditLogServicePort) {
        this.auditLogServicePort = auditLogServicePort;
    }

    @EventListener
    public void handle(final PickupPointChanged event) {
        final PickupPointSnapshot snapshot = event.getSnapshot();
        this.auditLogServicePort.record(new PickupPointAuditLog(snapshot.pickupPointId(), auditLogType(event),
                changes(event)));
    }

    private PickupPointAuditLogType auditLogType(final PickupPointChanged event) {
        return switch (event) {
            case final PickupPointCreated ignored -> PickupPointAuditLogType.CREATED;
            case final PickupPointUpdated ignored -> PickupPointAuditLogType.UPDATED;
            case final PickupPointSuspended ignored -> PickupPointAuditLogType.SUSPENDED;
            case final PickupPointResumed ignored -> PickupPointAuditLogType.RESUMED;
            case final PickupPointClosed ignored -> PickupPointAuditLogType.CLOSED;
            default -> throw new IllegalArgumentException("Unsupported pickup point event " + event.getClass());
        };
    }

    private List<FieldDifference> changes(final PickupPointChanged event) {
        return event.getPreviousSnapshot()
                .map(previousSnapshot -> SnapshotDifference.between(previousSnapshot, event.getSnapshot(),
                        IGNORED_FIELDS))
                .orElseGet(List::of);
    }
}
