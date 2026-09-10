package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.deliverynetwork.application.exception.DepartmentDirectoryImportMismatchException;
import com.warehouse.deliverynetwork.application.exception.IncompleteDepartmentDirectoryImportException;
import com.warehouse.deliverynetwork.application.exception.UnknownDepartmentCodeException;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.exception.DuplicateDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.MissingSortingFacilityConnectionException;
import com.warehouse.deliverynetwork.domain.exception.SelfDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.UnavailableDepartmentException;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DeliveryNetworkApiResponse;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.ReplaceDeliveryNetworkApiRequest;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkEditorApiMapper;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet.DeliveryNetworkSpreadsheetService;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet.InvalidDeliveryNetworkSpreadsheetException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@RestController
@RequestMapping("/delivery-networks/current")
@AccessUserControl
public class DeliveryNetworkController {

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
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidRequest(final MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body("Delivery network request is invalid");
    }
}
