package com.warehouse.shipment;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.port.primary.ShipmentContentPort;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentContentController;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentDocumentResponseApi;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentContentApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentContentControllerTest {

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(42L);

    @Mock
    private ShipmentContentPort shipmentContentPort;

    private ShipmentContentController controller;

    @BeforeEach
    void setUp() {
        this.controller = new ShipmentContentController(this.shipmentContentPort, new ShipmentContentApiMapper());
    }

    @Test
    void shouldStoreJpegPhotoAsShipmentDocument() {
        final byte[] content = {(byte) 0xff, (byte) 0xd8, (byte) 0xff, (byte) 0xd9};
        final MockMultipartFile file = new MockMultipartFile("file", "incident-1.jpg", "image/jpeg", content);
        final ShipmentDocument document = new ShipmentDocument(UUID.randomUUID(), SHIPMENT_ID, "incident-1.jpg",
                "image/jpeg", content.length, new UserId(7L), Instant.parse("2026-11-24T10:00:00Z"));
        when(this.shipmentContentPort.addDocument(eq(SHIPMENT_ID), eq("incident-1.jpg"), eq("image/jpeg"),
                any(byte[].class))).thenReturn(document);

        final ResponseEntity<ShipmentDocumentResponseApi> response = this.controller.addPhoto(42L, file);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        verify(this.shipmentContentPort).addDocument(SHIPMENT_ID, "incident-1.jpg", "image/jpeg", content);
    }

    @Test
    void shouldKeepExistingGeneralDocumentUploadsWorking() {
        final byte[] content = {1, 2, 3};
        final MockMultipartFile file = new MockMultipartFile("file", "invoice.pdf", "application/pdf", content);
        final ShipmentDocument document = new ShipmentDocument(UUID.randomUUID(), SHIPMENT_ID, "invoice.pdf",
                "application/pdf", content.length, new UserId(7L), Instant.parse("2026-11-24T10:00:00Z"));
        when(this.shipmentContentPort.addDocument(eq(SHIPMENT_ID), eq("invoice.pdf"), eq("application/pdf"),
                any(byte[].class))).thenReturn(document);

        final ResponseEntity<ShipmentDocumentResponseApi> response = this.controller.addDocument(42L, file);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        verify(this.shipmentContentPort).addDocument(SHIPMENT_ID, "invoice.pdf", "application/pdf", content);
    }

    @Test
    void shouldRejectOtherDocumentTypesFromPhotoEndpoint() {
        final MockMultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", new byte[]{1});

        assertThatThrownBy(() -> this.controller.addPhoto(42L, file)).isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(this.shipmentContentPort);
    }

    @Test
    void shouldRejectNonJpegDataEvenWhenMimeTypeIsJpeg() {
        final MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});

        assertThatThrownBy(() -> this.controller.addPhoto(42L, file)).isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(this.shipmentContentPort);
    }

    @Test
    void shouldRejectEmptyAndOversizedPhotos() {
        final MockMultipartFile empty = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);
        final MockMultipartFile oversized = new MockMultipartFile("file", "large.jpg", "image/jpeg",
                new byte[5 * 1024 * 1024 + 1]);

        assertThatThrownBy(() -> this.controller.addPhoto(42L, empty)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> this.controller.addPhoto(42L, oversized)).isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(this.shipmentContentPort);
    }

    @Test
    void shouldAllowSupplierPhotoUploadsWithoutAllowingGeneralDocumentUploads() throws NoSuchMethodException {
        final AccessUserControl photos = ShipmentContentController.class.getMethod("addPhoto", Long.class,
                MultipartFile.class).getAnnotation(AccessUserControl.class);
        final AccessUserControl documents = ShipmentContentController.class.getMethod("addDocument", Long.class,
                MultipartFile.class).getAnnotation(AccessUserControl.class);

        assertThat(photos.permissions()).contains(UserPermission.ROLE_SUPPLIER_CREATE);
        assertThat(documents.permissions()).doesNotContain(UserPermission.ROLE_SUPPLIER_CREATE);
    }
}
