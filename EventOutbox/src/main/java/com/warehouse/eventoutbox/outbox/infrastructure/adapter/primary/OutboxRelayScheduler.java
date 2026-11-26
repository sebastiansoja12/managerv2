package com.warehouse.eventoutbox.outbox.infrastructure.adapter.primary;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.warehouse.eventoutbox.outbox.application.service.OutboxCleanup;
import com.warehouse.eventoutbox.outbox.application.service.OutboxRelay;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboxRelayScheduler implements SmartLifecycle {

    private final OutboxRelay relay;
    private final OutboxCleanup cleanup;
    private final Duration pollInterval;
    private final Duration cleanupInterval;
    private final ThreadPoolTaskScheduler taskScheduler;

    private final AtomicBoolean wakeUpScheduled = new AtomicBoolean();

    private volatile boolean running;

    public OutboxRelayScheduler(final OutboxRelay relay,
                                final OutboxCleanup cleanup,
                                final Duration pollInterval,
                                final Duration cleanupInterval) {
        this.relay = relay;
        this.cleanup = cleanup;
        this.pollInterval = pollInterval;
        this.cleanupInterval = cleanupInterval;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(1);
        this.taskScheduler.setThreadNamePrefix("outbox-relay-");
    }

    @Override
    public void start() {
        this.taskScheduler.initialize();
        this.taskScheduler.scheduleWithFixedDelay(this.relay::publishPending, this.pollInterval);
        this.taskScheduler.scheduleWithFixedDelay(this.cleanup::deletePublished, this.cleanupInterval);
        this.running = true;
        log.info("[outbox] relay started with poll interval {} and cleanup interval {}",
                this.pollInterval, this.cleanupInterval);
    }

    public void wakeUp() {
        if (!this.running || !this.wakeUpScheduled.compareAndSet(false, true)) {
            return;
        }
        try {
            this.taskScheduler.execute(this::publishAfterWakeUp);
        } catch (final TaskRejectedException exception) {
            this.wakeUpScheduled.set(false);
            log.warn("[outbox] relay wake-up was rejected; scheduled polling will publish pending events");
        }
    }

    private void publishAfterWakeUp() {
        this.wakeUpScheduled.set(false);
        try {
            this.relay.publishPending();
        } catch (final RuntimeException exception) {
            log.error("[outbox] relay run after wake-up failed; scheduled polling will retry", exception);
        }
    }

    @Override
    public void stop() {
        this.taskScheduler.shutdown();
        this.running = false;
        log.info("[outbox] relay stopped");
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}
