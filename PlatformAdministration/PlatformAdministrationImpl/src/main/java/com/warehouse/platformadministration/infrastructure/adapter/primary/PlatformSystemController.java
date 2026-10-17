package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.platformadministration.application.port.primary.MaintenanceModePort;
import com.warehouse.platformadministration.application.port.primary.SystemStatusPort;
import com.warehouse.platformadministration.application.port.primary.command.ChangeMaintenanceModeCommand;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ChangeMaintenanceModeRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.MaintenanceModeResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.SystemStatusResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.PlatformSystemResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platform/system")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class PlatformSystemController {

    private final SystemStatusPort systemStatusPort;

    private final MaintenanceModePort maintenanceModePort;

    public PlatformSystemController(final SystemStatusPort systemStatusPort,
                                    final MaintenanceModePort maintenanceModePort) {
        this.systemStatusPort = systemStatusPort;
        this.maintenanceModePort = maintenanceModePort;
    }

    @GetMapping("/status")
    public ResponseEntity<SystemStatusResponse> status() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(PlatformSystemResponseMapper.toResponse(systemStatusPort.current()));
    }

    @GetMapping("/maintenance")
    public ResponseEntity<MaintenanceModeResponse> maintenance() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(PlatformSystemResponseMapper.toResponse(maintenanceModePort.current()));
    }

    @PutMapping("/maintenance")
    public ResponseEntity<MaintenanceModeResponse> changeMaintenance(
            @Valid @RequestBody final ChangeMaintenanceModeRequest request) {
        return ResponseEntity.ok(PlatformSystemResponseMapper.toResponse(maintenanceModePort.change(
                new ChangeMaintenanceModeCommand(request.enabled(), request.message()))));
    }

    @ExceptionHandler(PlatformActorRequiredException.class)
    public ResponseEntity<String> handlePlatformActorRequired(final PlatformActorRequiredException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
