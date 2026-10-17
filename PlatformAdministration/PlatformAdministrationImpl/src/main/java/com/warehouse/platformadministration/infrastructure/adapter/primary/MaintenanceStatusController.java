package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.platformadministration.application.port.primary.MaintenanceModePort;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.PublicMaintenanceResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.PlatformSystemResponseMapper;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceStatusController {

    private final MaintenanceModePort maintenanceModePort;

    public MaintenanceStatusController(final MaintenanceModePort maintenanceModePort) {
        this.maintenanceModePort = maintenanceModePort;
    }

    @GetMapping
    public ResponseEntity<PublicMaintenanceResponse> current() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(PlatformSystemResponseMapper.toPublicResponse(maintenanceModePort.current()));
    }
}
