package com.warehouse.auditlog.application.port.primary;

import com.warehouse.auditlog.application.port.primary.command.FindAuditLogEntriesCommand;
import com.warehouse.auditlog.application.port.primary.command.RecordAuditLogCommand;
import com.warehouse.auditlog.application.port.primary.result.AuditLogEntryPage;
import com.warehouse.auditlog.application.port.secondary.AuditLogActorServicePort;
import com.warehouse.auditlog.application.port.secondary.AuditLogEntryRepository;
import com.warehouse.auditlog.domain.model.AuditLogEntry;
import org.springframework.transaction.annotation.Transactional;

public class AuditLogPortImpl implements AuditLogPort {

    private static final int MAX_PAGE_SIZE = 200;

    private final AuditLogEntryRepository auditLogEntryRepository;

    private final AuditLogActorServicePort auditLogActorServicePort;

    public AuditLogPortImpl(final AuditLogEntryRepository auditLogEntryRepository,
                            final AuditLogActorServicePort auditLogActorServicePort) {
        this.auditLogEntryRepository = auditLogEntryRepository;
        this.auditLogActorServicePort = auditLogActorServicePort;
    }

    @Override
    @Transactional
    public void record(final RecordAuditLogCommand command) {
        if (command.eventId() != null && this.auditLogEntryRepository.exists(command.eventId())) {
            return;
        }
        this.auditLogActorServicePort.currentActor()
                .map(actor -> AuditLogEntry.record(command.eventId(), command.resource(), command.action(),
                        command.changes(), actor, command.occurredAt()))
                .ifPresent(this.auditLogEntryRepository::save);
    }

    @Override
    public AuditLogEntryPage find(final FindAuditLogEntriesCommand command) {
        return this.auditLogEntryRepository.find(new FindAuditLogEntriesCommand(command.resourceType(),
                command.resourceId(), command.userId(), Math.max(command.page(), 0),
                Math.clamp(command.size(), 1, MAX_PAGE_SIZE)));
    }
}
