package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.auditlog.api.dto.AuditLogChangeDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceIdDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceTypeDto;
import com.warehouse.auditlog.api.event.AuditLogEntryIntegrationEvent;
import com.warehouse.auditlog.api.event.snapshot.AuditLogEntryData;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.pickuppoint.application.port.secondary.AuditLogServicePort;
import com.warehouse.pickuppoint.domain.vo.PickupPointAuditLog;

import java.time.Instant;

public class AuditLogServiceAdapter implements AuditLogServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public AuditLogServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void record(final PickupPointAuditLog pickupPointAuditLog) {
        this.integrationEventPublisher.publish(new AuditLogEntryIntegrationEvent(new AuditLogEntryData(
                AuditLogResourceTypeDto.PICKUP_POINT,
                AuditLogResourceIdDto.of(pickupPointAuditLog.pickupPointId().value()),
                pickupPointAuditLog.type().name(),
                pickupPointAuditLog.changes().stream()
                        .map(change -> new AuditLogChangeDto(change.field(), change.previousValue(),
                                change.currentValue()))
                        .toList(),
                Instant.now())));
    }
}
