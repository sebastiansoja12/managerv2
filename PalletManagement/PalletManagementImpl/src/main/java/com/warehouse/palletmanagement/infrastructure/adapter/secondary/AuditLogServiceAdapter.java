package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.auditlog.api.dto.AuditLogChangeDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceIdDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceTypeDto;
import com.warehouse.auditlog.api.event.AuditLogEntryIntegrationEvent;
import com.warehouse.auditlog.api.event.snapshot.AuditLogEntryData;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.palletmanagement.application.port.secondary.AuditLogServicePort;
import com.warehouse.palletmanagement.domain.vo.PalletAuditLog;

import java.time.Instant;

public class AuditLogServiceAdapter implements AuditLogServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public AuditLogServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void record(final PalletAuditLog palletAuditLog) {
        this.integrationEventPublisher.publish(new AuditLogEntryIntegrationEvent(new AuditLogEntryData(
                AuditLogResourceTypeDto.PALLET,
                AuditLogResourceIdDto.of(palletAuditLog.palletId().value()),
                palletAuditLog.type().name(),
                palletAuditLog.changes().stream()
                        .map(change -> new AuditLogChangeDto(change.field(), change.previousValue(),
                                change.currentValue()))
                        .toList(),
                Instant.now())));
    }
}
