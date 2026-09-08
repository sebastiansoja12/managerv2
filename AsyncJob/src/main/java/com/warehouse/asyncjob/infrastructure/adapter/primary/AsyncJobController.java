package com.warehouse.asyncjob.infrastructure.adapter.primary;

import com.warehouse.asyncjob.application.port.primary.AsyncJobPort;
import com.warehouse.asyncjob.application.port.primary.command.CreateReadModelRebuildJobCommand;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.exception.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.AsyncJobResponse;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.CreateAsyncJobResponse;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.RebuildReadModelRequest;
import com.warehouse.asyncjob.infrastructure.adapter.primary.mapper.AsyncJobResponseMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/admin/async-jobs")
@Slf4j
public class AsyncJobController {

    private final AsyncJobPort asyncJobPort;
    private final AsyncJobResponseMapper responseMapper;

    public AsyncJobController(final AsyncJobPort asyncJobPort, final AsyncJobResponseMapper responseMapper) {
        this.asyncJobPort = asyncJobPort;
        this.responseMapper = responseMapper;
    }

    @PostMapping("/read-models/{type}/rebuild")
    public ResponseEntity<CreateAsyncJobResponse> rebuildReadModel(@PathVariable final ReadModelType type,
                                                                   @Valid @RequestBody final RebuildReadModelRequest request) {
        validateDateRange(request);
        log.info("Async job for {} started", type);
        final AsyncJobSnapshot job = this.asyncJobPort.createReadModelRebuildJob(new CreateReadModelRebuildJobCommand(
                type, OperatorId.of(request.operatorId()), request.dateFrom(), request.dateTo()));
        return ResponseEntity.accepted().body(new CreateAsyncJobResponse(job.id().value()));
    }

    @GetMapping("/{jobId}")
    public AsyncJobResponse get(@PathVariable final UUID jobId) {
        return this.responseMapper.toResponse(this.asyncJobPort.get(new AsyncJobId(jobId)));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AsyncJobNotFoundException.class)
    public void handleNotFound() {
    }

    private void validateDateRange(final RebuildReadModelRequest request) {
        if (request.dateFrom().isAfter(request.dateTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateFrom must be less than or equal to dateTo");
        }
    }
}
