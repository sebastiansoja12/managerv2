package com.warehouse.auditlog.application.port.primary;

import com.warehouse.auditlog.application.port.primary.command.FindAuditLogEntriesCommand;
import com.warehouse.auditlog.application.port.primary.command.RecordAuditLogCommand;
import com.warehouse.auditlog.application.port.secondary.AuditLogActorServicePort;
import com.warehouse.auditlog.application.port.secondary.AuditLogEntryRepository;
import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.auditlog.domain.model.AuditLogEntry;
import com.warehouse.auditlog.domain.vo.AuditLogActor;
import com.warehouse.auditlog.domain.vo.AuditLogChange;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import com.warehouse.auditlog.domain.vo.AuditLogResource;
import com.warehouse.auditlog.domain.vo.AuditLogResourceId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogPortImplTest {

    private static final Instant OCCURRED_AT = Instant.parse("2026-11-01T10:46:22Z");

    private static final AuditLogEventId EVENT_ID = new AuditLogEventId(UUID.randomUUID());

    private static final AuditLogResource SHIPMENT = new AuditLogResource(AuditLogResourceType.SHIPMENT,
            new AuditLogResourceId("42"));

    private static final AuditLogActor ACTOR = new AuditLogActor(OperatorId.of(1L), new DepartmentId(2L),
            new UserId(3L), new Username("jan.kowalski"));

    @Mock
    private AuditLogEntryRepository auditLogEntryRepository;

    @Mock
    private AuditLogActorServicePort auditLogActorServicePort;

    private AuditLogPortImpl auditLogPort;

    @BeforeEach
    void setUp() {
        auditLogPort = new AuditLogPortImpl(auditLogEntryRepository, auditLogActorServicePort);
    }

    @Test
    void shouldRecordWhatChangedAndWhoChangedIt() {
        when(auditLogEntryRepository.exists(EVENT_ID)).thenReturn(false);
        when(auditLogActorServicePort.currentActor()).thenReturn(Optional.of(ACTOR));
        final List<AuditLogChange> changes = List.of(new AuditLogChange("shipmentStatus", "ACCEPTED", "SENT"));

        auditLogPort.record(new RecordAuditLogCommand(EVENT_ID, SHIPMENT, "SENT", changes, OCCURRED_AT));

        final ArgumentCaptor<AuditLogEntry> entryCaptor = ArgumentCaptor.forClass(AuditLogEntry.class);
        verify(auditLogEntryRepository).save(entryCaptor.capture());
        final AuditLogEntry entry = entryCaptor.getValue();
        assertEquals(EVENT_ID, entry.eventId());
        assertEquals(SHIPMENT, entry.resource());
        assertEquals("SENT", entry.action());
        assertEquals(changes, entry.changes());
        assertEquals(ACTOR, entry.actor());
        assertEquals(OCCURRED_AT, entry.occurredAt());
    }

    @Test
    void shouldIgnoreAlreadyRecordedEvent() {
        when(auditLogEntryRepository.exists(EVENT_ID)).thenReturn(true);

        auditLogPort.record(new RecordAuditLogCommand(EVENT_ID, SHIPMENT, "SENT", List.of(), OCCURRED_AT));

        verify(auditLogEntryRepository, never()).save(any());
    }

    @Test
    void shouldNotRecordWhenOperatorContextIsMissing() {
        when(auditLogEntryRepository.exists(EVENT_ID)).thenReturn(false);
        when(auditLogActorServicePort.currentActor()).thenReturn(Optional.empty());

        auditLogPort.record(new RecordAuditLogCommand(EVENT_ID, SHIPMENT, "CREATED", List.of(), OCCURRED_AT));

        verify(auditLogEntryRepository, never()).save(any());
    }

    @Test
    void shouldClampPageSize() {
        auditLogPort.find(new FindAuditLogEntriesCommand(AuditLogResourceType.SHIPMENT, null, null, -1, 1000));

        verify(auditLogEntryRepository).find(new FindAuditLogEntriesCommand(AuditLogResourceType.SHIPMENT, null,
                null, 0, 200));
    }
}
