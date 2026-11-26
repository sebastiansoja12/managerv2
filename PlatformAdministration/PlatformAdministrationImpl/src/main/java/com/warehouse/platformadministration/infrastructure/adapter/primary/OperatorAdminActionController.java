package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.OperatorAdminActionPort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.DepartmentAssignmentRunResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorJobRunResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.OperatorAdminActionResponseMapper;
import jakarta.validation.constraints.Positive;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Validated
@RestController
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorAdminActionController {

    private final OperatorAdminActionPort operatorAdminActionPort;

    public OperatorAdminActionController(final OperatorAdminActionPort operatorAdminActionPort) {
        this.operatorAdminActionPort = operatorAdminActionPort;
    }

    @GetMapping("/platform/operator-jobs")
    public ResponseEntity<List<String>> findJobTypes() {
        return ResponseEntity.ok(operatorAdminActionPort.jobTypes().stream().map(OperatorJobType::name).toList());
    }

    @PostMapping("/operators/{operatorId}/admin-actions/jobs/{jobType}")
    public ResponseEntity<OperatorJobRunResponse> runJob(@PathVariable @Positive final Long operatorId,
                                                         @PathVariable final OperatorJobType jobType) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(OperatorAdminActionResponseMapper.toResponse(
                        operatorAdminActionPort.runJob(OperatorId.of(operatorId), jobType)));
    }

    @GetMapping("/operators/{operatorId}/admin-actions/courier-assignment-runs")
    public ResponseEntity<List<DepartmentAssignmentRunResponse>> findCourierAssignmentRuns(
            @PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(operatorAdminActionPort.findCourierAssignmentRuns(OperatorId.of(operatorId)).stream()
                        .map(OperatorAdminActionResponseMapper::toResponse)
                        .toList());
    }

    @ExceptionHandler(OperatorNotFoundException.class)
    public ResponseEntity<String> handleOperatorNotFound(final OperatorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(final IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> handleBadRequest(final RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
