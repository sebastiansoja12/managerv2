package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.application.exception.DepartmentDirectoryImportMismatchException;
import com.warehouse.deliverynetwork.application.exception.IncompleteDepartmentDirectoryImportException;
import com.warehouse.deliverynetwork.application.exception.ShipmentOriginDepartmentNotAssignedException;
import com.warehouse.deliverynetwork.application.exception.UnknownDepartmentCodeException;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.application.port.primary.result.ShipmentDeliveryRouteResult;
import com.warehouse.deliverynetwork.domain.exception.DeliveryPathNotFoundException;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;
import com.warehouse.deliverynetwork.domain.exception.DuplicateDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.MissingSortingFacilityConnectionException;
import com.warehouse.deliverynetwork.domain.exception.SelfDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.UnavailableDepartmentException;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DeliveryNetworkApiResponse;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.ReplaceDeliveryNetworkApiRequest;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.ShipmentDeliveryRouteApiResponse;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkEditorApiMapper;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet.DeliveryNetworkSpreadsheetService;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet.InvalidDeliveryNetworkSpreadsheetException;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/delivery-networks/current")
@AccessUserControl
public class DeliveryNetworkController {

    private static final Locale DEFAULT_DOCUMENT_LOCALE = Locale.of("pl");

    private static final Set<String> SUPPORTED_DOCUMENT_LANGUAGES = Set.of("pl", "en", "de");

    private final DeliveryNetworkPort deliveryNetworkPort;

    private final DeliveryNetworkEditorApiMapper deliveryNetworkEditorApiMapper;

    private final DeliveryNetworkSpreadsheetService deliveryNetworkSpreadsheetService;

    public DeliveryNetworkController(
            final DeliveryNetworkPort deliveryNetworkPort,
            final DeliveryNetworkEditorApiMapper deliveryNetworkEditorApiMapper,
            final DeliveryNetworkSpreadsheetService deliveryNetworkSpreadsheetService) {
        this.deliveryNetworkPort = deliveryNetworkPort;
        this.deliveryNetworkEditorApiMapper = deliveryNetworkEditorApiMapper;
        this.deliveryNetworkSpreadsheetService = deliveryNetworkSpreadsheetService;
    }

    @GetMapping
    public ResponseEntity<DeliveryNetworkApiResponse> getCurrentNetwork() {
        final DeliveryNetworkResult deliveryNetwork = this.deliveryNetworkPort.getCurrentNetwork();
        return ResponseEntity.ok(this.deliveryNetworkEditorApiMapper.toResponse(deliveryNetwork));
    }

    @PutMapping
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<DeliveryNetworkApiResponse> replaceCurrentNetwork(
            @Valid @RequestBody final ReplaceDeliveryNetworkApiRequest request) {
        final ReplaceDeliveryNetworkCommand command = this.deliveryNetworkEditorApiMapper.toCommand(request);
        final DeliveryNetworkResult deliveryNetwork = this.deliveryNetworkPort.replaceCurrentNetwork(command);
        return ResponseEntity.ok(this.deliveryNetworkEditorApiMapper.toResponse(deliveryNetwork));
    }

    @GetMapping("/export")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<byte[]> exportCurrentNetwork() {
        final DeliveryNetworkExportResult deliveryNetwork = this.deliveryNetworkPort.getCurrentNetworkForExport();
        final byte[] workbook = this.deliveryNetworkSpreadsheetService.exportWorkbook(deliveryNetwork);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + DeliveryNetworkSpreadsheetService.FILE_NAME + "\"")
                .contentType(MediaType.parseMediaType(DeliveryNetworkSpreadsheetService.CONTENT_TYPE))
                .body(workbook);
    }

    @GetMapping("/shipment-routes/{trackingNumber}")
    public ResponseEntity<ShipmentDeliveryRouteApiResponse> getShipmentDeliveryRoute(
            @PathVariable final String trackingNumber) {
        final ShipmentDeliveryRouteResult shipmentDeliveryRoute =
                this.deliveryNetworkPort.findShipmentDeliveryRoute(new TrackingNumber(trackingNumber.trim()));
        return ResponseEntity.ok(this.deliveryNetworkEditorApiMapper.toResponse(shipmentDeliveryRoute));
    }

    @GetMapping(value = "/shipment-routes/{trackingNumber}/documents/route", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getShipmentRouteDocument(
            @PathVariable final String trackingNumber,
            @RequestParam(name = "language", required = false) final String language) {
        return pdf(this.deliveryNetworkPort.generateShipmentRouteDocument(new TrackingNumber(trackingNumber.trim()),
                documentLocale(language)));
    }

    @GetMapping(value = "/shipment-routes/{trackingNumber}/documents/handling-plan",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getShipmentHandlingPlan(
            @PathVariable final String trackingNumber,
            @RequestParam(name = "language", required = false) final String language) {
        return pdf(this.deliveryNetworkPort.generateShipmentHandlingPlan(new TrackingNumber(trackingNumber.trim()),
                documentLocale(language)));
    }

    private static Locale documentLocale(final String language) {
        if (language == null || language.isBlank()) {
            return DEFAULT_DOCUMENT_LOCALE;
        }
        final Locale locale = Locale.forLanguageTag(language.trim());
        return SUPPORTED_DOCUMENT_LANGUAGES.contains(locale.getLanguage())
                ? Locale.of(locale.getLanguage())
                : DEFAULT_DOCUMENT_LOCALE;
    }

    private ResponseEntity<byte[]> pdf(final ShipmentRouteDocumentFile file) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(file.fileName())
                        .build()
                        .toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(file.content());
    }

    @PutMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<DeliveryNetworkApiResponse> importCurrentNetwork(
            @RequestPart("file") final MultipartFile file) {
        validateImportFile(file);
        final ReplaceDeliveryNetworkByCodesCommand command = readImportCommand(file);
        final DeliveryNetworkResult deliveryNetwork =
                this.deliveryNetworkPort.replaceCurrentNetworkByDepartmentCodes(command);
        return ResponseEntity.ok(this.deliveryNetworkEditorApiMapper.toResponse(deliveryNetwork));
    }

    private void validateImportFile(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidDeliveryNetworkSpreadsheetException("The Excel file cannot be empty");
        }
        if (file.getSize() > DeliveryNetworkSpreadsheetService.MAX_FILE_SIZE) {
            throw new InvalidDeliveryNetworkSpreadsheetException("The Excel file cannot exceed 5 MB");
        }
        final String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
            throw new InvalidDeliveryNetworkSpreadsheetException("Select a file in .xlsx format");
        }
    }

    private ReplaceDeliveryNetworkByCodesCommand readImportCommand(final MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return this.deliveryNetworkSpreadsheetService.importWorkbook(inputStream);
        } catch (final IOException exception) {
            throw new InvalidDeliveryNetworkSpreadsheetException("Could not read the Excel file", exception);
        }
    }

    @ExceptionHandler({
            DuplicateDepartmentConnectionException.class,
            MissingSortingFacilityConnectionException.class,
            SelfDepartmentConnectionException.class,
            UnavailableDepartmentException.class,
            UnknownDepartmentException.class,
            UnknownDepartmentCodeException.class,
            IncompleteDepartmentDirectoryImportException.class,
            DepartmentDirectoryImportMismatchException.class,
            InvalidDeliveryNetworkSpreadsheetException.class
    })
    public ResponseEntity<String> handleInvalidNetwork(final RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({
            ShipmentOriginDepartmentNotAssignedException.class,
            DeliveryPathNotFoundException.class
    })
    public ResponseEntity<String> handleUnavailableRoute(final RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidRequest(final MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body("Delivery network request is invalid");
    }
}
