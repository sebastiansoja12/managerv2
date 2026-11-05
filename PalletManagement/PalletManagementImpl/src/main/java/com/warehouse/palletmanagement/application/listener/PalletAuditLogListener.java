package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.helper.SnapshotDifference;
import com.warehouse.palletmanagement.application.port.secondary.AuditLogServicePort;
import com.warehouse.palletmanagement.domain.enumeration.PalletAuditLogType;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.palletmanagement.domain.vo.PalletAuditLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class PalletAuditLogListener {

    private static final Set<String> IGNORED_FIELDS = Set.of("palletId");

    private final AuditLogServicePort auditLogServicePort;

    public PalletAuditLogListener(final AuditLogServicePort auditLogServicePort) {
        this.auditLogServicePort = auditLogServicePort;
    }

    @EventListener
    public void handle(final PalletLoaded event) {
        this.auditLogServicePort.record(new PalletAuditLog(event.getSnapshot().palletId(),
                PalletAuditLogType.LOADED, SnapshotDifference.of(event.getSnapshot(), IGNORED_FIELDS)));
    }

    @EventListener
    public void handle(final PalletLoadingReverted event) {
        this.auditLogServicePort.record(new PalletAuditLog(event.getSnapshot().palletId(),
                PalletAuditLogType.LOADING_REVERTED, SnapshotDifference.of(event.getSnapshot(), IGNORED_FIELDS)));
    }

    @EventListener
    public void handle(final PalletUnloaded event) {
        this.auditLogServicePort.record(new PalletAuditLog(event.getSnapshot().palletId(),
                PalletAuditLogType.UNLOADED, SnapshotDifference.of(event.getSnapshot(), IGNORED_FIELDS)));
    }
}
