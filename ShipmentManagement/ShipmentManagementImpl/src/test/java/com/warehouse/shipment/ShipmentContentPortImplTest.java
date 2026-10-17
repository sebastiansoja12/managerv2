package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.port.primary.ShipmentContentPortImpl;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentContentRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.exception.ShipmentNotFoundException;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.domain.model.ShipmentNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ShipmentContentPortImplTest {

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(42L);

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentContentRepository contentRepository;

    @Mock
    private ShipmentActorServicePort actorServicePort;

    private ShipmentContentPortImpl shipmentContentPort;

    @BeforeEach
    void setUp() {
        shipmentContentPort = new ShipmentContentPortImpl(shipmentRepository, contentRepository, actorServicePort);
    }

    @Test
    void shouldSaveInternalNoteWithAuthor() {
        when(shipmentRepository.exists(SHIPMENT_ID)).thenReturn(true);
        when(actorServicePort.currentUserId()).thenReturn(new UserId(7L));

        final ShipmentNote note = shipmentContentPort.addNote(SHIPMENT_ID, "  Handle with care  ", true);

        final ArgumentCaptor<ShipmentNote> noteCaptor = ArgumentCaptor.forClass(ShipmentNote.class);
        verify(contentRepository).saveNote(noteCaptor.capture());
        assertEquals("Handle with care", noteCaptor.getValue().body());
        assertTrue(noteCaptor.getValue().internal());
        assertEquals(new UserId(7L), noteCaptor.getValue().authorUserId());
        assertEquals(note, noteCaptor.getValue());
    }

    @Test
    void shouldRejectNoteWhenShipmentDoesNotExist() {
        when(shipmentRepository.exists(SHIPMENT_ID)).thenReturn(false);

        assertThrows(ShipmentNotFoundException.class,
                () -> shipmentContentPort.addNote(SHIPMENT_ID, "Missing shipment", false));

        verify(contentRepository, never()).saveNote(any());
    }

    @Test
    void shouldSaveDocumentWithNormalizedFilename() {
        when(shipmentRepository.exists(SHIPMENT_ID)).thenReturn(true);
        when(actorServicePort.currentUserId()).thenReturn(new UserId(7L));
        final byte[] content = {1, 2, 3};

        shipmentContentPort.addDocument(SHIPMENT_ID, "folder\\invoice.pdf", "application/pdf", content);

        final ArgumentCaptor<ShipmentDocumentFile> documentCaptor = ArgumentCaptor.forClass(ShipmentDocumentFile.class);
        verify(contentRepository).saveDocument(documentCaptor.capture());
        assertEquals("invoice.pdf", documentCaptor.getValue().document().filename());
        assertArrayEquals(content, documentCaptor.getValue().content());
    }

    @Test
    void shouldRejectUnsupportedDocumentType() {
        when(shipmentRepository.exists(SHIPMENT_ID)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> shipmentContentPort.addDocument(SHIPMENT_ID, "page.html", "text/html", new byte[] {1}));

        verify(contentRepository, never()).saveDocument(any());
    }
}
