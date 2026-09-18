package com.warehouse.auditlog.infrastructure.adapter.secondary;

import com.warehouse.auditlog.application.port.primary.command.FindAuditLogEntriesCommand;
import com.warehouse.auditlog.application.port.primary.result.AuditLogEntryPage;
import com.warehouse.auditlog.application.port.secondary.AuditLogEntryRepository;
import com.warehouse.auditlog.domain.model.AuditLogEntry;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import com.warehouse.auditlog.infrastructure.adapter.secondary.entity.AuditLogEntryEntity;
import com.warehouse.auditlog.infrastructure.adapter.secondary.mapper.AuditLogEntryPersistenceMapper;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.List;

public class AuditLogEntryRepositoryImpl implements AuditLogEntryRepository {

    private final OperatorFilteredRepository<AuditLogEntryEntity> repository;

    private final AuditLogEntryPersistenceMapper persistenceMapper;

    public AuditLogEntryRepositoryImpl(final OperatorFilteredRepository<AuditLogEntryEntity> repository,
                                       final AuditLogEntryPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void save(final AuditLogEntry auditLogEntry) {
        this.repository.create(this.persistenceMapper.toEntity(auditLogEntry));
    }

    @Override
    public boolean exists(final AuditLogEventId eventId) {
        return this.repository.createCriteria(AuditLogEntryEntity.class)
                .eq("eventId", eventId.value())
                .count() > 0;
    }

    @Override
    public AuditLogEntryPage find(final FindAuditLogEntriesCommand command) {
        final long totalElements = criteria(command).count();
        final List<AuditLogEntry> entries = criteria(command)
                .desc("occurredAt")
                .firstResult(command.page() * command.size())
                .maxResults(command.size())
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
        return new AuditLogEntryPage(entries, command.page(), command.size(), totalElements);
    }

    private Criteria<AuditLogEntryEntity> criteria(final FindAuditLogEntriesCommand command) {
        final Criteria<AuditLogEntryEntity> criteria = this.repository.createCriteria(AuditLogEntryEntity.class);
        if (command.resourceType() != null) {
            criteria.eq("resourceType", command.resourceType());
        }
        if (command.resourceId() != null) {
            criteria.eq("resourceId", command.resourceId().value());
        }
        if (command.userId() != null) {
            criteria.eq("userId", command.userId().value());
        }
        return criteria;
    }
}
