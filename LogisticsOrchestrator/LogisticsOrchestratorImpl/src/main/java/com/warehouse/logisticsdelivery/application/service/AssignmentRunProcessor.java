package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentBatch;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunMetrics;
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierQuery;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.domain.model.CourierRoster;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTimeRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionOperations;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class AssignmentRunProcessor {

    private static final Logger log = LoggerFactory.getLogger(AssignmentRunProcessor.class);

    private final DepartmentAssignmentRunRepository runRepository;

    private final DeliveryRepository deliveryRepository;

    private final CourierRosterLoader courierRosterLoader;

    private final AssignmentBatchAssigner batchAssigner;

    private final AssignmentRunMetrics metrics;

    private final TransactionOperations transactionOperations;

    private final Clock clock;

    private final String workerId;

    private final Duration lease;

    private final int maxRetries;

    private final int infoLogEveryBatches;

    public AssignmentRunProcessor(final DepartmentAssignmentRunRepository runRepository,
                                  final DeliveryRepository deliveryRepository,
                                  final CourierRosterLoader courierRosterLoader,
                                  final AssignmentBatchAssigner batchAssigner,
                                  final AssignmentRunMetrics metrics,
                                  final TransactionOperations transactionOperations,
                                  final Clock clock,
                                  final String workerId,
                                  final Duration lease,
                                  final int maxRetries,
                                  final int infoLogEveryBatches) {
        this.runRepository = runRepository;
        this.deliveryRepository = deliveryRepository;
        this.courierRosterLoader = courierRosterLoader;
        this.batchAssigner = batchAssigner;
        this.metrics = metrics;
        this.transactionOperations = transactionOperations;
        this.clock = clock;
        this.workerId = workerId;
        this.lease = lease;
        this.maxRetries = maxRetries;
        this.infoLogEveryBatches = infoLogEveryBatches;
    }

    public void process(final DepartmentAssignmentRunId runId) {
        final Instant claimedAt = this.clock.instant();
        final Boolean claimed = this.transactionOperations.execute(status ->
                this.runRepository.claim(runId, this.workerId, claimedAt, claimedAt.plus(this.lease)));
        if (!Boolean.TRUE.equals(claimed)) {
            log.debug("Department assignment run {} is already leased or finished", runId.value());
            return;
        }
        final DepartmentAssignmentRun run = Objects.requireNonNull(this.transactionOperations.execute(status ->
                this.runRepository.findById(runId).orElseThrow()));
        final AssignmentWindow window = run.getWindow();
        log.info("Department assignment run {} of department {} started: window {} - {} ({} - {} local), "
                        + "backlog from {}, resuming from {}",
                runId.value(), run.getDepartmentId().getValue(), window.from(), window.to(),
                window.from().atZone(this.clock.getZone()).toLocalDateTime(),
                window.to().atZone(this.clock.getZone()).toLocalDateTime(), window.backlogFrom(), run.getCursor());
        try {
            CourierRoster roster = this.courierRosterLoader.load(run.getDepartmentId());
            if (roster.isEmpty()) {
                finish(run, () -> run.stop("No active couriers with a delivery area in the department",
                        this.clock.instant()), claimedAt);
                return;
            }
            final DeliveryTimeRange range = new DeliveryTimeRange(toStorageTime(window.backlogFrom()),
                    toStorageTime(window.to()));
            AssignmentBatch batch;
            do {
                final CourierRoster batchRoster = roster;
                batch = Objects.requireNonNull(this.transactionOperations.execute(status -> {
                    final AssignmentBatch processed = this.batchAssigner.assign(
                            this.deliveryRepository.findAwaitingCourier(new AwaitingCourierQuery(run.getDepartmentId(),
                                    run.getDateField(), range, run.getCursor(), run.getBatchSize())),
                            run.getCursor(), run.getBatchSize(), batchRoster);
                    final Instant now = this.clock.instant();
                    run.recordBatch(processed.summary(), processed.nextCursor(), processed.maxEligibleAt(), now,
                            this.lease);
                    if (processed.lastBatch()) {
                        run.complete(now);
                    }
                    this.runRepository.save(run);
                    return processed;
                }));
                roster = batch.roster();
                logBatch(run, batch);
            } while (!batch.lastBatch());
            recordFinished(run, claimedAt);
        } catch (final RuntimeException exception) {
            registerError(runId, exception, claimedAt);
        }
    }

    private void finish(final DepartmentAssignmentRun run, final Runnable transition, final Instant claimedAt) {
        this.transactionOperations.executeWithoutResult(status -> {
            transition.run();
            this.runRepository.save(run);
        });
        recordFinished(run, claimedAt);
    }

    private void registerError(final DepartmentAssignmentRunId runId, final RuntimeException exception,
                               final Instant claimedAt) {
        log.warn("Department assignment run {} failed; batch rolled back, run resumes from its last cursor", runId.value(),
                exception);
        this.transactionOperations.executeWithoutResult(status -> this.runRepository.findById(runId)
                .filter(fresh -> fresh.isLeasedBy(this.workerId))
                .ifPresent(fresh -> {
                    fresh.registerError(exception.getMessage(), this.clock.instant(), this.maxRetries);
                    this.runRepository.save(fresh);
                    if (fresh.getRetries() >= this.maxRetries) {
                        log.warn("Department assignment run {} of department {} reached {} retries and ends in ERROR",
                                runId.value(), fresh.getDepartmentId().getValue(), fresh.getRetries());
                        recordFinished(fresh, claimedAt);
                    }
                }));
    }

    private void recordFinished(final DepartmentAssignmentRun run, final Instant claimedAt) {
        final Duration duration = Duration.between(claimedAt, this.clock.instant());
        this.metrics.recordRun(run.snapshot(), duration);
        log.info("Department assignment run {} of department {} finished with {} in {} ms: {}",
                run.getRunId().value(), run.getDepartmentId().getValue(), run.getStatus(), duration.toMillis(),
                run.getSummary());
    }

    private void logBatch(final DepartmentAssignmentRun run, final AssignmentBatch batch) {
        final int batches = run.getSummary().batches();
        if (batches % this.infoLogEveryBatches == 0) {
            log.info("Department assignment run {} processed {} batches, cursor {}, totals {}", run.getRunId().value(),
                    batches, run.getCursor(), run.getSummary());
        } else {
            log.debug("Department assignment run {} batch {} cursor {}: {}", run.getRunId().value(), batches,
                    batch.nextCursor(), batch.summary());
        }
    }

    private LocalDateTime toStorageTime(final Instant instant) {
        return instant.atZone(this.clock.getZone()).toLocalDateTime();
    }
}
