package com.warehouse.asyncjob.application.port.secondary;

public interface AsyncJobSchedulerServicePort {

    void scheduleAfterCommit(final Runnable execution);
}
