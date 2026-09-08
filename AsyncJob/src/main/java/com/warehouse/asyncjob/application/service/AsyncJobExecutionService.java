package com.warehouse.asyncjob.application.service;

import com.warehouse.asyncjob.application.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.domain.exception.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

public class AsyncJobExecutionService {

    private static final Logger log = LoggerFactory.getLogger(AsyncJobExecutionService.class);

    private final AsyncJobRepository asyncJobRepository;
    private final ReadModelRebuildServiceRegistry rebuildServiceRegistry;
    private final TransactionTemplate transactionTemplate;

    public AsyncJobExecutionService(final AsyncJobRepository asyncJobRepository,
                                    final ReadModelRebuildServiceRegistry rebuildServiceRegistry,
                                    final TransactionTemplate transactionTemplate) {
        this.asyncJobRepository = asyncJobRepository;
        this.rebuildServiceRegistry = rebuildServiceRegistry;
        this.transactionTemplate = transactionTemplate;
    }

    public void execute(final AsyncJobId jobId) {
        final AsyncJob job = markRunning(jobId);
        try {
            this.rebuildServiceRegistry.rebuildService(job.getReadModelType())
                    .rebuild(job.getOperatorId(), job.getDateFrom(), job.getDateTo());
            markCompleted(jobId);
            log.info("Async job for {} read model finished", job.getReadModelType());
        } catch (final Exception exception) {
            markFailed(jobId, exception);
            log.warn("Async job for {} read model failed", job.getReadModelType());
        }
    }

    private AsyncJob markRunning(final AsyncJobId jobId) {
        return this.transactionTemplate.execute(status -> {
            final AsyncJob job = find(jobId);
            job.markRunning();
            return this.asyncJobRepository.save(job);
        });
    }

    private void markCompleted(final AsyncJobId jobId) {
        this.transactionTemplate.executeWithoutResult(status -> {
            final AsyncJob job = find(jobId);
            job.markCompleted();
            this.asyncJobRepository.save(job);
        });
    }

    private void markFailed(final AsyncJobId jobId, final Exception exception) {
        this.transactionTemplate.executeWithoutResult(status -> {
            final AsyncJob job = find(jobId);
            job.markFailed(exception.getMessage());
            this.asyncJobRepository.save(job);
        });
    }

    private AsyncJob find(final AsyncJobId jobId) {
        return this.asyncJobRepository.findById(jobId)
                .orElseThrow(() -> new AsyncJobNotFoundException(jobId));
    }
}
