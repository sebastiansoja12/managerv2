package com.warehouse.auditlog.infrastructure.adapter.primary.mapper;

import com.warehouse.auditlog.application.port.primary.command.FindAuditLogEntriesCommand;
import com.warehouse.auditlog.application.port.primary.result.AuditLogEntryPage;
import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.auditlog.domain.model.AuditLogEntry;
import com.warehouse.auditlog.domain.vo.AuditLogActor;
import com.warehouse.auditlog.domain.vo.AuditLogResourceId;
import com.warehouse.auditlog.infrastructure.adapter.primary.api.AuditLogChangeApiResponse;
import com.warehouse.auditlog.infrastructure.adapter.primary.api.AuditLogEntryApiResponse;
import com.warehouse.auditlog.infrastructure.adapter.primary.api.AuditLogEntryPageApiResponse;
import com.warehouse.commonassets.identificator.UserId;

public class AuditLogWebMapper {

    public FindAuditLogEntriesCommand toFindCommand(final AuditLogResourceType resourceType,
                                                    final String resourceId,
                                                    final Long userId,
                                                    final int page,
                                                    final int size) {
        return new FindAuditLogEntriesCommand(
                resourceType,
                resourceId == null || resourceId.isBlank() ? null : new AuditLogResourceId(resourceId),
                userId == null ? null : new UserId(userId),
                page,
                size);
    }

    public AuditLogEntryPageApiResponse toResponse(final AuditLogEntryPage auditLogEntryPage) {
        return new AuditLogEntryPageApiResponse(
                auditLogEntryPage.entries().stream().map(this::toResponse).toList(),
                auditLogEntryPage.page(),
                auditLogEntryPage.size(),
                auditLogEntryPage.totalElements());
    }

    private AuditLogEntryApiResponse toResponse(final AuditLogEntry auditLogEntry) {
        final AuditLogActor actor = auditLogEntry.actor();
        return new AuditLogEntryApiResponse(
                auditLogEntry.auditLogEntryId().value(),
                auditLogEntry.resource().type(),
                auditLogEntry.resource().resourceId().value(),
                auditLogEntry.action(),
                auditLogEntry.changes().stream()
                        .map(change -> new AuditLogChangeApiResponse(change.field(), change.previousValue(),
                                change.currentValue()))
                        .toList(),
                actor.userId() == null ? null : actor.userId().value(),
                actor.username() == null ? null : actor.username().value(),
                actor.system(),
                actor.departmentId() == null ? null : actor.departmentId().value(),
                auditLogEntry.occurredAt());
    }
}
