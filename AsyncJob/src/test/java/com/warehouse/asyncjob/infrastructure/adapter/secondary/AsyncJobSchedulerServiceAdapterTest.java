package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class AsyncJobSchedulerServiceAdapterTest {

    private final AsyncJobSchedulerServiceAdapter adapter = new AsyncJobSchedulerServiceAdapter(new SyncTaskExecutor());

    @AfterEach
    void tearDown() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void shouldExecuteImmediatelyWithoutActiveTransaction() {
        final AtomicBoolean executed = new AtomicBoolean(false);

        this.adapter.scheduleAfterCommit(() -> executed.set(true));

        assertThat(executed).isTrue();
    }

    @Test
    void shouldExecuteOnlyAfterCommit() {
        final AtomicBoolean executed = new AtomicBoolean(false);
        TransactionSynchronizationManager.initSynchronization();

        this.adapter.scheduleAfterCommit(() -> executed.set(true));

        assertThat(executed).isFalse();
        TransactionSynchronizationManager.getSynchronizations().forEach(TransactionSynchronization::afterCommit);
        assertThat(executed).isTrue();
    }
}
