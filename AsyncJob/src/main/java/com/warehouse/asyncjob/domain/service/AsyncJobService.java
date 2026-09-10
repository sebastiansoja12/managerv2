package com.warehouse.asyncjob.domain.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.model.ReadModelType;
import com.warehouse.asyncjob.domain.port.secondary.AsyncJobRepository;

@Service
public class AsyncJobService {

    private final AsyncJobRepository asyncJobRepository;
    private final AsyncJobWorker asyncJobWorker;

    public AsyncJobService(final AsyncJobRepository asyncJobRepository,
                           final AsyncJobWorker asyncJobWorker) {
        this.asyncJobRepository = asyncJobRepository;
        this.asyncJobWorker = asyncJobWorker;
    }

    @Transactional
    public AsyncJob createReadModelRebuildJob(final ReadModelType type,
                                              final Long operatorId,
                                              final LocalDate dateFrom,
                                              final LocalDate dateTo) {
        final AsyncJob job = this.asyncJobRepository.save(AsyncJob.pending(type, operatorId, dateFrom, dateTo));
        scheduleAfterCommit(job.getId());
        return job;
    }

    @Transactional(readOnly = true)
    public AsyncJob get(final UUID jobId) {
        return this.asyncJobRepository.findById(jobId)
                .orElseThrow(() -> new AsyncJobNotFoundException(jobId));
    }

    private void scheduleAfterCommit(final UUID jobId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            this.asyncJobWorker.rebuildReadModel(jobId);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                asyncJobWorker.rebuildReadModel(jobId);
            }
        });
    }
}
