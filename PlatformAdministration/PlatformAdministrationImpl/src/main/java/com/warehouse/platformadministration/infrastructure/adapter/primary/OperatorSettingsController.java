package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.OperatorSettingsPort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;
import jakarta.validation.constraints.Positive;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/operators/{operatorId}/settings")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorSettingsController {

    private final OperatorSettingsPort operatorSettingsPort;

    public OperatorSettingsController(final OperatorSettingsPort operatorSettingsPort) {
        this.operatorSettingsPort = operatorSettingsPort;
    }

    @GetMapping("/shipment")
    public ResponseEntity<Map<String, Object>> findShipmentSettings(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(operatorSettingsPort.findShipmentSettings(OperatorId.of(operatorId)).values());
    }

    @PutMapping("/shipment")
    public ResponseEntity<Map<String, Object>> updateShipmentSettings(@PathVariable @Positive final Long operatorId,
                                                                      @RequestBody final Map<String, Object> request) {
        return ResponseEntity.ok(operatorSettingsPort.updateShipmentSettings(OperatorId.of(operatorId),
                new OperatorShipmentSettings(request)).values());
    }

    @ExceptionHandler(OperatorNotFoundException.class)
    public ResponseEntity<String> handleOperatorNotFound(final OperatorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(final IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
