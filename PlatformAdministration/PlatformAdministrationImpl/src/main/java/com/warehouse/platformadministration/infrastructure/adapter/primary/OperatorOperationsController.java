package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.OperatorDataPort;
import com.warehouse.platformadministration.application.port.primary.OperatorDevicePort;
import com.warehouse.platformadministration.application.port.primary.OperatorOnboardingPort;
import com.warehouse.platformadministration.application.port.primary.command.EraseOperatorDataCommand;
import com.warehouse.platformadministration.domain.exception.OperatorDataErasureNotAllowedException;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ChangeDeviceStatusRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.EraseOperatorDataRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorApiKeyResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDataErasureResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDataExportResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDeviceResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorOnboardingResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.OperatorOperationsResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/operators/{operatorId}")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorOperationsController {

    private final OperatorOnboardingPort operatorOnboardingPort;

    private final OperatorDevicePort operatorDevicePort;

    private final OperatorDataPort operatorDataPort;

    public OperatorOperationsController(final OperatorOnboardingPort operatorOnboardingPort,
                                        final OperatorDevicePort operatorDevicePort,
                                        final OperatorDataPort operatorDataPort) {
        this.operatorOnboardingPort = operatorOnboardingPort;
        this.operatorDevicePort = operatorDevicePort;
        this.operatorDataPort = operatorDataPort;
    }

    @GetMapping("/onboarding")
    public ResponseEntity<OperatorOnboardingResponse> onboarding(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(OperatorOperationsResponseMapper.toResponse(operatorOnboardingPort.find(OperatorId.of(operatorId))));
    }

    @GetMapping("/devices")
    public ResponseEntity<List<OperatorDeviceResponse>> devices(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(operatorDevicePort.findDevices(OperatorId.of(operatorId))
                        .stream()
                        .map(OperatorOperationsResponseMapper::toResponse)
                        .toList());
    }

    @PostMapping("/devices/{deviceId}/revoke")
    public ResponseEntity<Void> revokeDevice(@PathVariable @Positive final Long operatorId,
                                             @PathVariable final String deviceId) {
        operatorDevicePort.revokeDevice(OperatorId.of(operatorId), deviceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/devices/{deviceId}/status")
    public ResponseEntity<Void> changeDeviceStatus(@PathVariable @Positive final Long operatorId,
                                                   @PathVariable final String deviceId,
                                                   @Valid @RequestBody final ChangeDeviceStatusRequest request) {
        operatorDevicePort.changeDeviceStatus(OperatorId.of(operatorId), deviceId, request.status());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api-keys")
    public ResponseEntity<List<OperatorApiKeyResponse>> apiKeys(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(operatorDevicePort.findApiKeys(OperatorId.of(operatorId))
                        .stream()
                        .map(OperatorOperationsResponseMapper::toResponse)
                        .toList());
    }

    @DeleteMapping("/api-keys/{userId}")
    public ResponseEntity<Void> revokeApiKey(@PathVariable @Positive final Long operatorId,
                                             @PathVariable @Positive final Long userId) {
        operatorDevicePort.revokeApiKey(OperatorId.of(operatorId), new UserId(userId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/data-export")
    public ResponseEntity<OperatorDataExportResponse> export(@PathVariable @Positive final Long operatorId) {
        final String fileName = "operator-" + operatorId + "-export-" + LocalDate.now(ZoneOffset.UTC) + ".json";
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString())
                .body(OperatorOperationsResponseMapper.toResponse(operatorDataPort.export(OperatorId.of(operatorId))));
    }

    @GetMapping("/data-erasure")
    public ResponseEntity<OperatorDataErasureResponse> lastErasure(@PathVariable @Positive final Long operatorId) {
        return operatorDataPort.findLastErasure(OperatorId.of(operatorId))
                .map(erasure -> ResponseEntity.ok()
                        .cacheControl(CacheControl.noStore())
                        .body(OperatorOperationsResponseMapper.toResponse(erasure)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/data-erasure")
    public ResponseEntity<OperatorDataErasureResponse> erase(@PathVariable @Positive final Long operatorId,
                                                             @Valid @RequestBody final EraseOperatorDataRequest request) {
        return ResponseEntity.ok(OperatorOperationsResponseMapper.toResponse(operatorDataPort.erase(
                OperatorId.of(operatorId), new EraseOperatorDataCommand(request.confirmation()))));
    }

    @ExceptionHandler(OperatorNotFoundException.class)
    public ResponseEntity<String> handleOperatorNotFound(final OperatorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(PlatformActorRequiredException.class)
    public ResponseEntity<String> handlePlatformActorRequired(final PlatformActorRequiredException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(OperatorDataErasureNotAllowedException.class)
    public ResponseEntity<Map<String, String>> handleErasureNotAllowed(final OperatorDataErasureNotAllowedException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("reason", exception.getReason(), "message", exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(final IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
