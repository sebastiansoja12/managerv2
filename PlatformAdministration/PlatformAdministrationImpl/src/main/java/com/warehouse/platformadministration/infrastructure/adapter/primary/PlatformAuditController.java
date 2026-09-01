package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.PlatformAuditPort;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.PlatformAuditEntryResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.PlatformAdministrationResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platform/audit-log")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class PlatformAuditController {

    private final PlatformAuditPort platformAuditPort;

    public PlatformAuditController(final PlatformAuditPort platformAuditPort) {
        this.platformAuditPort = platformAuditPort;
    }

    @GetMapping
    public ResponseEntity<List<PlatformAuditEntryResponse>> findLatest(
            @RequestParam(required = false) final Long operatorId,
            @RequestParam(defaultValue = "100") final int limit) {
        return ResponseEntity.ok(platformAuditPort.findLatest(operatorId != null ? OperatorId.of(operatorId) : null, limit)
                .stream()
                .map(PlatformAdministrationResponseMapper::toResponse)
                .toList());
    }
}
