package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.application.listener.ShipmentAuditLogListener;
import com.warehouse.shipment.application.port.secondary.AuditLogServicePort;
import com.warehouse.shipment.domain.enumeration.ShipmentAuditLogType;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
import com.warehouse.shipment.domain.event.ShipmentRecipientChanged;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.service.ShipmentChangeDetector;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentAuditLog;
import com.warehouse.shipment.domain.vo.ShipmentFieldChange;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ShipmentAuditLogListenerTest {

    @Mock
    private AuditLogServicePort auditLogServicePort;

    private ShipmentAuditLogListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new ShipmentAuditLogListener(this.auditLogServicePort,
                new ShipmentChangeDetector());
    }

    @Test
    void shouldRecordChangedRecipientFields() {
        final Shipment shipment = DataTestCreator.shipment();
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.changeRecipient(Party.builder()
                .firstName("test")
                .lastName("test")
                .city("Gliwice")
                .street("test")
                .postalCode("44-100")
                .telephoneNumber("123")
                .email("test@test.pl")
                .countryCode(CountryCode.DE)
                .build());

        this.listener.handle(new ShipmentRecipientChanged(previousSnapshot, shipment.snapshot(), Instant.now()));

        final ArgumentCaptor<ShipmentAuditLog> auditLogCaptor = ArgumentCaptor.forClass(ShipmentAuditLog.class);
        verify(this.auditLogServicePort).record(auditLogCaptor.capture());
        assertEquals(shipment.getShipmentId(), auditLogCaptor.getValue().shipmentId());
        assertEquals(ShipmentAuditLogType.RECIPIENT_CHANGED, auditLogCaptor.getValue().type());
        assertEquals(List.of(
                new ShipmentFieldChange("recipient.postalCode", "00-000", "44-100"),
                new ShipmentFieldChange("recipient.city", "test", "Gliwice")), auditLogCaptor.getValue().changes());
    }

    @Test
    void shouldRecordCurrentStatusWhenPreviousSnapshotIsMissing() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentStatusChanged(shipment.snapshot(), Instant.now()));

        final ArgumentCaptor<ShipmentAuditLog> auditLogCaptor = ArgumentCaptor.forClass(ShipmentAuditLog.class);
        verify(this.auditLogServicePort).record(auditLogCaptor.capture());
        assertEquals(ShipmentAuditLogType.STATUS_CHANGED, auditLogCaptor.getValue().type());
        assertEquals(List.of(new ShipmentFieldChange("shipmentStatus", null,
                String.valueOf(shipment.getShipmentStatus()))), auditLogCaptor.getValue().changes());
    }

    @Test
    void shouldIgnoreDestinationChangedBecauseRecipientChangeIsRecorded() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));

        verifyNoInteractions(this.auditLogServicePort);
    }
}
