package com.warehouse.auditlog.infrastructure.adapter.secondary.mapper;

import com.warehouse.auditlog.domain.model.AuditLogEntry;
import com.warehouse.auditlog.domain.vo.AuditLogActor;
import com.warehouse.auditlog.domain.vo.AuditLogChange;
import com.warehouse.auditlog.domain.vo.AuditLogEntryId;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import com.warehouse.auditlog.domain.vo.AuditLogResource;
import com.warehouse.auditlog.domain.vo.AuditLogResourceId;
import com.warehouse.auditlog.infrastructure.adapter.secondary.entity.AuditLogChangeEmbeddable;
import com.warehouse.auditlog.infrastructure.adapter.secondary.entity.AuditLogEntryEntity;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;

public class AuditLogEntryPersistenceMapper {

    public AuditLogEntryEntity toEntity(final AuditLogEntry auditLogEntry) {
        final AuditLogActor actor = auditLogEntry.actor();
        final AuditLogEntryEntity entity = new AuditLogEntryEntity(
                auditLogEntry.eventId() == null ? null : auditLogEntry.eventId().value(),
                auditLogEntry.resource().type(),
                auditLogEntry.resource().resourceId().value(),
                auditLogEntry.action(),
                auditLogEntry.changes().stream()
                        .map(change -> new AuditLogChangeEmbeddable(change.field(), change.previousValue(),
                                change.currentValue()))
                        .toList(),
                actor.departmentId() == null ? null : actor.departmentId().value(),
                actor.userId() == null ? null : actor.userId().value(),
                actor.username() == null ? null : actor.username().value(),
                auditLogEntry.occurredAt());
        entity.assignOperator(actor.operatorId());
        return entity;
    }

    public AuditLogEntry toDomain(final AuditLogEntryEntity entity) {
        return new AuditLogEntry(
                new AuditLogEntryId(entity.getId()),
                entity.getEventId() == null ? null : new AuditLogEventId(entity.getEventId()),
                new AuditLogResource(entity.getResourceType(), new AuditLogResourceId(entity.getResourceId())),
                entity.getAction(),
                entity.getChanges().stream()
                        .map(change -> new AuditLogChange(change.getField(), change.getPreviousValue(),
                                change.getCurrentValue()))
                        .toList(),
                new AuditLogActor(
                        entity.operatorId(),
                        entity.getDepartmentId() == null ? null : new DepartmentId(entity.getDepartmentId()),
                        entity.getUserId() == null ? null : new UserId(entity.getUserId()),
                        entity.getUsername() == null ? null : new Username(entity.getUsername())),
                entity.getOccurredAt());
    }
}
