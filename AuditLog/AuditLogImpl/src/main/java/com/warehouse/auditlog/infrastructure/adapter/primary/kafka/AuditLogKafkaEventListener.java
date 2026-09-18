package com.warehouse.auditlog.infrastructure.adapter.primary.kafka;

import com.warehouse.auditlog.api.event.AuditLogEntryIntegrationEvent;
import com.warehouse.auditlog.application.port.primary.AuditLogPort;
import com.warehouse.auditlog.infrastructure.adapter.primary.mapper.AuditLogEventMapper;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
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

    @KafkaEventListener(
            topics = "${manager.kafka.topics.audit-log-entries:audit-log.entries}",
            groupId = "${manager.kafka.consumer-groups.audit-log:audit-log}"
    )
    public void handle(final AuditLogEntryIntegrationEvent event) {
        this.auditLogPort.record(this.auditLogEventMapper.toCommand(event));
    }
}
