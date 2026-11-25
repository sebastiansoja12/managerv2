package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.application.port.primary.ShipmentContentPort;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentDocumentResponseApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentNoteRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentNoteResponseApi;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentContentApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shipments/{shipmentId}")
@AccessUserControl
public class ShipmentContentController {

    private static final long MAX_DOCUMENT_SIZE = 5L * 1024 * 1024;

    private final ShipmentContentPort shipmentContentPort;
    private final ShipmentContentApiMapper mapper;

    public ShipmentContentController(final ShipmentContentPort shipmentContentPort,
                                     final ShipmentContentApiMapper mapper) {
        this.shipmentContentPort = shipmentContentPort;
        this.mapper = mapper;
    }

    @PostMapping("/notes")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
    public ResponseEntity<ShipmentNoteResponseApi> addNote(@PathVariable final Long shipmentId,
                                                            @Valid @RequestBody final ShipmentNoteRequestApi request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(
                shipmentContentPort.addNote(new ShipmentId(shipmentId), request.body(), request.internal())));
    }

    @GetMapping("/notes")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public List<ShipmentNoteResponseApi> listNotes(@PathVariable final Long shipmentId) {
        return shipmentContentPort.listNotes(new ShipmentId(shipmentId)).stream()
                .map(mapper::toResponse).toList();
    }

    @PostMapping(path = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
    public ResponseEntity<ShipmentDocumentResponseApi> addDocument(@PathVariable final Long shipmentId,
                                                                    @RequestPart("file") final MultipartFile file) {
        return storeDocument(new ShipmentId(shipmentId), file, readDocument(file));
    }

    @PostMapping(path = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE,
            UserPermission.ROLE_SUPPLIER_CREATE})
    public ResponseEntity<ShipmentDocumentResponseApi> addPhoto(@PathVariable final Long shipmentId,
                                                               @RequestPart("file") final MultipartFile file) {
        if (!MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())) {
            throw new IllegalArgumentException("Photo must be a JPEG image");
        }
        final byte[] content = readDocument(file);
        if (content.length < 3 || content[0] != (byte) 0xff || content[1] != (byte) 0xd8
                || content[2] != (byte) 0xff) {
            throw new IllegalArgumentException("Photo must contain JPEG image data");
        }
        return storeDocument(new ShipmentId(shipmentId), file, content);
    }

    private byte[] readDocument(final MultipartFile file) {
        if (file.isEmpty() || file.getSize() > MAX_DOCUMENT_SIZE) {
            throw new IllegalArgumentException("Document must contain 1 byte to 5 MB");
        }
        try {
            return file.getBytes();
        } catch (final IOException exception) {
            throw new RestException(400, "Document could not be read");
        }
    }

    private ResponseEntity<ShipmentDocumentResponseApi> storeDocument(final ShipmentId shipmentId,
                                                                     final MultipartFile file,
                                                                     final byte[] content) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(
                shipmentContentPort.addDocument(shipmentId, file.getOriginalFilename(), file.getContentType(), content)));
    }

    @GetMapping("/documents")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public List<ShipmentDocumentResponseApi> listDocuments(@PathVariable final Long shipmentId) {
        return shipmentContentPort.listDocuments(new ShipmentId(shipmentId)).stream()
                .map(mapper::toResponse).toList();
    }

    @GetMapping("/documents/{documentId}/download")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<byte[]> downloadDocument(@PathVariable final Long shipmentId,
                                                   @PathVariable final UUID documentId) {
        final ShipmentDocumentFile documentFile = shipmentContentPort.getDocument(new ShipmentId(shipmentId), documentId);
        final ContentDisposition disposition = ContentDisposition.attachment()
                .filename(documentFile.document().filename(), StandardCharsets.UTF_8).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .header("X-Content-Type-Options", "nosniff")
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .contentType(MediaType.parseMediaType(documentFile.document().contentType()))
                .contentLength(documentFile.document().sizeBytes())
                .body(documentFile.content());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidContent(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidNote(final MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body("Shipment content request is invalid");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleOversizedDocument(final MaxUploadSizeExceededException exception) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Document cannot exceed 5 MB");
    }
}
