package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.application.port.secondary.AsyncJobSchedulerServicePort;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class AsyncJobSchedulerServiceAdapter implements AsyncJobSchedulerServicePort {

    private final TaskExecutor taskExecutor;

    public AsyncJobSchedulerServiceAdapter(final TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void scheduleAfterCommit(final Runnable execution) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            this.taskExecutor.execute(execution);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                taskExecutor.execute(execution);
            }
        });
    }
}
