package com.warehouse.auditlog.infrastructure.adapter.primary.kafka;

import com.warehouse.auditlog.api.event.AuditLogEntryIntegrationEvent;
import com.warehouse.auditlog.application.port.primary.AuditLogPort;
import com.warehouse.auditlog.infrastructure.adapter.primary.mapper.AuditLogEventMapper;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogKafkaEventListener {

    private final AuditLogPort auditLogPort;

    private final AuditLogEventMapper auditLogEventMapper;

    public AuditLogKafkaEventListener(final AuditLogPort auditLogPort,
                                      final AuditLogEventMapper auditLogEventMapper) {
        this.auditLogPort = auditLogPort;
        this.auditLogEventMapper = auditLogEventMapper;
    }

    @IntegrationEventListener(group = "manager-audit-log")
    public void handle(final AuditLogEntryIntegrationEvent event) {
        this.auditLogPort.record(this.auditLogEventMapper.toCommand(event));
    }
}
