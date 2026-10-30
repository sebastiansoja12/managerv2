package com.warehouse.auditlog.infrastructure.adapter.primary.mapper;

import com.warehouse.auditlog.api.dto.AuditLogChangeDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceIdDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceTypeDto;
import com.warehouse.auditlog.api.event.AuditLogEntryIntegrationEvent;
import com.warehouse.auditlog.api.event.snapshot.AuditLogEntryData;
import com.warehouse.auditlog.application.port.primary.command.RecordAuditLogCommand;
import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.auditlog.domain.vo.AuditLogChange;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuditLogEventMapperTest {

    private static final AuditLogEntryData PAYLOAD = new AuditLogEntryData(AuditLogResourceTypeDto.PALLET,
            new AuditLogResourceIdDto("PAL-1"), "LOADED",
            List.of(new AuditLogChangeDto("shipmentIds", null, "[1, 2]")), Instant.parse("2026-11-01T10:00:00Z"));

    private final AuditLogEventMapper auditLogEventMapper = new AuditLogEventMapper();

    @Test
    void shouldMapKafkaEventToRecordCommand() {
        final UUID eventId = UUID.randomUUID();

        final RecordAuditLogCommand command = auditLogEventMapper.toCommand(
                new AuditLogEntryIntegrationEvent(PAYLOAD, eventId));

        assertEquals(new AuditLogEventId(eventId), command.eventId());
        assertEquals(AuditLogResourceType.PALLET, command.resource().type());
        assertEquals("PAL-1", command.resource().resourceId().value());
        assertEquals("LOADED", command.action());
        assertEquals(List.of(new AuditLogChange("shipmentIds", null, "[1, 2]")), command.changes());
        assertEquals(PAYLOAD.occurredAt(), command.occurredAt());
    }

    @Test
    void shouldLeaveEventIdEmptyWhenKafkaMessageHasNone() {
        final RecordAuditLogCommand command = auditLogEventMapper.toCommand(new AuditLogEntryIntegrationEvent(PAYLOAD));

        assertNull(command.eventId());
    }
}
