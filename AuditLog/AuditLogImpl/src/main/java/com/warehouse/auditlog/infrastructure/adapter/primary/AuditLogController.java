package com.warehouse.auditlog.infrastructure.adapter.primary;

import com.warehouse.auditlog.application.port.primary.AuditLogPort;
import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.auditlog.infrastructure.adapter.primary.api.AuditLogEntryPageApiResponse;
import com.warehouse.auditlog.infrastructure.adapter.primary.mapper.AuditLogWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit-log")
public class AuditLogController {

    private final AuditLogPort auditLogPort;

    private final AuditLogWebMapper auditLogWebMapper;

    public AuditLogController(final AuditLogPort auditLogPort,
                                    final AuditLogWebMapper auditLogWebMapper) {
        this.auditLogPort = auditLogPort;
        this.auditLogWebMapper = auditLogWebMapper;
    }

    @GetMapping
    public ResponseEntity<AuditLogEntryPageApiResponse> search(
            @RequestParam(required = false) final AuditLogResourceType resourceType,
            @RequestParam(required = false) final String resourceId,
            @RequestParam(required = false) final Long userId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        return ResponseEntity.ok(this.auditLogWebMapper.toResponse(this.auditLogPort.find(
                this.auditLogWebMapper.toFindCommand(resourceType, resourceId, userId, page, size))));
    }

    @GetMapping("/{resourceType}/{resourceId}")
    public ResponseEntity<AuditLogEntryPageApiResponse> findByResource(
            @PathVariable final AuditLogResourceType resourceType,
            @PathVariable final String resourceId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        return ResponseEntity.ok(this.auditLogWebMapper.toResponse(this.auditLogPort.find(
                this.auditLogWebMapper.toFindCommand(resourceType, resourceId, null, page, size))));
    }
}
