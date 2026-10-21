package com.warehouse.asyncjob.application.port.primary;

import com.warehouse.asyncjob.application.port.primary.command.CreateReadModelRebuildJobCommand;
import com.warehouse.asyncjob.application.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.application.port.secondary.AsyncJobSchedulerServicePort;
import com.warehouse.asyncjob.application.service.AsyncJobExecutionService;
import com.warehouse.asyncjob.domain.exception.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.commonassets.identificator.OperatorId;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AsyncJobPortImpl implements AsyncJobPort {

    private final AsyncJobRepository asyncJobRepository;
    private final AsyncJobSchedulerServicePort asyncJobSchedulerServicePort;
    private final AsyncJobExecutionService asyncJobExecutionService;

    public AsyncJobPortImpl(final AsyncJobRepository asyncJobRepository,
                            final AsyncJobSchedulerServicePort asyncJobSchedulerServicePort,
                            final AsyncJobExecutionService asyncJobExecutionService) {
        this.asyncJobRepository = asyncJobRepository;
        this.asyncJobSchedulerServicePort = asyncJobSchedulerServicePort;
        this.asyncJobExecutionService = asyncJobExecutionService;
    }

    @Override
    @Transactional
    public AsyncJobSnapshot createReadModelRebuildJob(final CreateReadModelRebuildJobCommand command) {
        final AsyncJob job = this.asyncJobRepository.save(AsyncJob.pending(command.readModelType(),
                command.operatorId(), command.dateFrom(), command.dateTo()));
        final AsyncJobId jobId = job.getId();
        this.asyncJobSchedulerServicePort.scheduleAfterCommit(() -> this.asyncJobExecutionService.execute(jobId));
        return job.snapshot();
    }

    @Override
    @Transactional(readOnly = true)
    public AsyncJobSnapshot get(final AsyncJobId jobId) {
        return this.asyncJobRepository.findById(jobId)
                .map(AsyncJob::snapshot)
                .orElseThrow(() -> new AsyncJobNotFoundException(jobId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsyncJobSnapshot> findLatest(final OperatorId operatorId, final int limit) {
        return this.asyncJobRepository.findLatestByOperatorId(operatorId, Math.max(1, Math.min(limit, 100))).stream()
                .map(AsyncJob::snapshot)
                .toList();
    }
}
