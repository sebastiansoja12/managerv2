package com.warehouse.asyncjob.infrastructure.adapter.primary;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.model.ReadModelType;
import com.warehouse.asyncjob.domain.service.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.service.AsyncJobService;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.AsyncJobResponse;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.CreateAsyncJobResponse;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.RebuildReadModelRequest;
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

    private final AsyncJobService asyncJobService;

    public AsyncJobController(final AsyncJobService asyncJobService) {
        this.asyncJobService = asyncJobService;
    }

    @PostMapping("/read-models/{type}/rebuild")
    public ResponseEntity<CreateAsyncJobResponse> rebuildReadModel(@PathVariable final ReadModelType type,
                                                                   @Valid @RequestBody final RebuildReadModelRequest request) {
        validateDateRange(request);
        log.info("=====Async job for {} started=====", type);
        final AsyncJob job = this.asyncJobService.createReadModelRebuildJob(type, request.operatorId(),
                request.dateFrom(), request.dateTo());
        return ResponseEntity.accepted().body(new CreateAsyncJobResponse(job.getId()));
    }

    @GetMapping("/{jobId}")
    public AsyncJobResponse get(@PathVariable final UUID jobId) {
        return AsyncJobResponse.from(this.asyncJobService.get(jobId));
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
