package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.port.primary.ShipmentContentPort;
import com.warehouse.shipment.application.port.primary.ShipmentIncidentPortImpl;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentNoteCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentResolutionCommand;
import com.warehouse.shipment.domain.enumeration.IncidentNoteType;
import com.warehouse.shipment.domain.enumeration.IncidentShipmentAction;
import com.warehouse.shipment.domain.vo.ShipmentNoteIncidentSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ShipmentIncidentPortImplTest {

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(42L);
    private static final Instant OCCURRED_AT = Instant.parse("2026-11-07T10:00:00Z");

    @Mock
    private ShipmentPort shipmentPort;

    @Mock
    private ShipmentReadModelSyncPort shipmentReadModelSyncPort;

    @Mock
    private ShipmentContentPort shipmentContentPort;

    private ShipmentIncidentPortImpl shipmentIncidentPort;

    @BeforeEach
    void setUp() {
        shipmentIncidentPort = new ShipmentIncidentPortImpl(shipmentPort, shipmentReadModelSyncPort, shipmentContentPort);
    }

    @Test
    void shouldMarkIncidentAndAddReasonNoteWhenReported() {
        final ShipmentIncidentNoteCommand command = note(IncidentNoteType.REPORTED, "DAMAGED", null);

        shipmentIncidentPort.reportIncident(command);

        verify(shipmentReadModelSyncPort).syncIncidentReported(SHIPMENT_ID, true);
        verifyNote(command);
        verifyNoInteractions(shipmentPort);
    }

    @Test
    void shouldOnlyAddNoteWhenCommented() {
        final ShipmentIncidentNoteCommand command = note(IncidentNoteType.COMMENTED, null, "Called the recipient");

        shipmentIncidentPort.commentIncident(command);

        verify(shipmentReadModelSyncPort, never()).syncIncidentReported(eq(SHIPMENT_ID), anyBoolean());
        verifyNote(command);
    }

    @Test
    void shouldApplyResolutionActionThenClearIncidentThenAddNote() {
        final ShipmentIncidentNoteCommand note = note(IncidentNoteType.RESOLVED, "WRITTEN_OFF", "Not found");

        shipmentIncidentPort.resolveIncident(new ShipmentIncidentResolutionCommand(note,
                IncidentShipmentAction.MARK_LOST, null));

        final InOrder order = inOrder(shipmentPort, shipmentReadModelSyncPort, shipmentContentPort);
        order.verify(shipmentPort).markLost(SHIPMENT_ID);
        order.verify(shipmentReadModelSyncPort).syncIncidentReported(SHIPMENT_ID, false);
        order.verify(shipmentContentPort).addIncidentNote(SHIPMENT_ID, note.incidentSource(), "Not found",
                note.authorUserId(), OCCURRED_AT);
    }

    @Test
    void shouldResolveWithoutShipmentChangeWhenNoActionIsRequired() {
        final ShipmentIncidentNoteCommand note = note(IncidentNoteType.RESOLVED, "FOUND", "Department KT1");

        shipmentIncidentPort.resolveIncident(new ShipmentIncidentResolutionCommand(note, null, null));

        verifyNoInteractions(shipmentPort);
        verify(shipmentReadModelSyncPort).syncIncidentReported(SHIPMENT_ID, false);
        verifyNote(note);
    }

    private void verifyNote(final ShipmentIncidentNoteCommand command) {
        verify(shipmentContentPort).addIncidentNote(SHIPMENT_ID, command.incidentSource(), command.text(),
                command.authorUserId(), OCCURRED_AT);
    }

    private ShipmentIncidentNoteCommand note(final IncidentNoteType type, final String reason, final String text) {
        return new ShipmentIncidentNoteCommand(SHIPMENT_ID,
                new ShipmentNoteIncidentSource(new IncidentId(7L), type, reason), text, new UserId(3L), OCCURRED_AT);
    }
}
