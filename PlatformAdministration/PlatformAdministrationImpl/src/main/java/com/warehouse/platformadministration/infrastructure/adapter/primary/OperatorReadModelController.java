package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.ReadModelRebuildPort;
import com.warehouse.platformadministration.application.port.primary.command.StartReadModelRebuildCommand;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.exception.ReadModelRebuildJobNotFoundException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ReadModelRebuildJobResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.StartReadModelRebuildRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.PlatformSystemResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorReadModelController {

    private final ReadModelRebuildPort readModelRebuildPort;

    public OperatorReadModelController(final ReadModelRebuildPort readModelRebuildPort) {
        this.readModelRebuildPort = readModelRebuildPort;
    }

    @GetMapping("/platform/read-models/types")
    public ResponseEntity<List<String>> findReadModelTypes() {
        return ResponseEntity.ok(readModelRebuildPort.readModelTypes());
    }

    @GetMapping("/platform/read-models/jobs/{jobId}")
    public ResponseEntity<ReadModelRebuildJobResponse> findJob(@PathVariable final UUID jobId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(PlatformSystemResponseMapper.toResponse(readModelRebuildPort.find(jobId)));
    }

    @GetMapping("/operators/{operatorId}/read-models/jobs")
    public ResponseEntity<List<ReadModelRebuildJobResponse>> findLatestJobs(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(readModelRebuildPort.findLatest(OperatorId.of(operatorId))
                        .stream()
                        .map(PlatformSystemResponseMapper::toResponse)
                        .toList());
    }

    @PostMapping("/operators/{operatorId}/read-models/{readModelType}/rebuild")
    public ResponseEntity<ReadModelRebuildJobResponse> rebuild(@PathVariable @Positive final Long operatorId,
                                                               @PathVariable final String readModelType,
                                                               @Valid @RequestBody final StartReadModelRebuildRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(PlatformSystemResponseMapper.toResponse(readModelRebuildPort.start(OperatorId.of(operatorId),
                        new StartReadModelRebuildCommand(readModelType, request.dateFrom(), request.dateTo()))));
    }

    @ExceptionHandler({OperatorNotFoundException.class, ReadModelRebuildJobNotFoundException.class})
    public ResponseEntity<String> handleNotFound(final RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
