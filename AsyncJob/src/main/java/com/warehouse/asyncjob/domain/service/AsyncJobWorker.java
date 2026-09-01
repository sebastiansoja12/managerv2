package com.warehouse.asyncjob.domain.service;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.port.secondary.AsyncJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
@Slf4j
public class AsyncJobWorker {

    private final AsyncJobRepository asyncJobRepository;
    private final ReadModelRebuilderRegistry rebuilderRegistry;
    private final TransactionTemplate transactionTemplate;

    public AsyncJobWorker(final AsyncJobRepository asyncJobRepository,
                          final ReadModelRebuilderRegistry rebuilderRegistry,
                          final TransactionTemplate transactionTemplate) {
        this.asyncJobRepository = asyncJobRepository;
        this.rebuilderRegistry = rebuilderRegistry;
        this.transactionTemplate = transactionTemplate;
    }

    @Async
    public void rebuildReadModel(final UUID jobId) {
        final AsyncJob job = markRunning(jobId);
        try {
            this.rebuilderRegistry.rebuilder(job.getReadModelType())
                    .rebuild(job.getOperatorId(), job.getDateFrom(), job.getDateTo());
            markCompleted(jobId);
            log.info("=====Async job for {} read model finished=====", job.getReadModelType());
        } catch (final Exception exception) {
            markFailed(jobId, exception);
            log.info("=====Async job for {} read model failed=====", job.getReadModelType());
        }
    }

    private AsyncJob markRunning(final UUID jobId) {
        return this.transactionTemplate.execute(status -> {
            final AsyncJob job = find(jobId);
            job.markRunning();
            return this.asyncJobRepository.save(job);
        });
    }

    private void markCompleted(final UUID jobId) {
        this.transactionTemplate.executeWithoutResult(status -> {
            final AsyncJob job = find(jobId);
            job.markCompleted();
            this.asyncJobRepository.save(job);
        });
    }

    private void markFailed(final UUID jobId, final Exception exception) {
        this.transactionTemplate.executeWithoutResult(status -> {
            final AsyncJob job = find(jobId);
            job.markFailed(exception);
            this.asyncJobRepository.save(job);
        });
    }

    private AsyncJob find(final UUID jobId) {
        return this.asyncJobRepository.findById(jobId)
                .orElseThrow(() -> new AsyncJobNotFoundException(jobId));
    }
}
