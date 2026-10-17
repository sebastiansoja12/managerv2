package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.OperatorAccessPort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorAccessResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.PlatformSystemResponseMapper;
import jakarta.validation.constraints.Positive;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/operators/{operatorId}/access")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorAccessController {

    private final OperatorAccessPort operatorAccessPort;

    public OperatorAccessController(final OperatorAccessPort operatorAccessPort) {
        this.operatorAccessPort = operatorAccessPort;
    }

    @GetMapping
    public ResponseEntity<OperatorAccessResponse> findAccess(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(PlatformSystemResponseMapper.toResponse(operatorAccessPort.findAccess(OperatorId.of(operatorId))));
    }

    @PostMapping("/sessions/revoke")
    public ResponseEntity<Void> revokeSessions(@PathVariable @Positive final Long operatorId) {
        operatorAccessPort.revokeSessions(OperatorId.of(operatorId));
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(OperatorNotFoundException.class)
    public ResponseEntity<String> handleOperatorNotFound(final OperatorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
