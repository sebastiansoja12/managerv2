package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentOrchestration;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentConfigServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.domain.event.DepartmentAssignmentRunCreated;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.service.AssignmentWindowCalculator;
import com.warehouse.logisticsdelivery.domain.service.DepartmentAssignmentRunFactory;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionOperations;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class AssignmentOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AssignmentOrchestrator.class);

    private final DepartmentAssignmentConfigServicePort configServicePort;

    private final DepartmentAssignmentRunRepository runRepository;

    private final DepartmentAssignmentRunFactory runFactory;

    private final AssignmentWindowCalculator windowCalculator;

    private final DomainEventPublisher domainEventPublisher;

    private final TransactionOperations transactionOperations;

    private final Clock clock;

    public AssignmentOrchestrator(final DepartmentAssignmentConfigServicePort configServicePort,
                                  final DepartmentAssignmentRunRepository runRepository,
                                  final DepartmentAssignmentRunFactory runFactory,
                                  final AssignmentWindowCalculator windowCalculator,
                                  final DomainEventPublisher domainEventPublisher,
                                  final TransactionOperations transactionOperations,
                                  final Clock clock) {
        this.configServicePort = configServicePort;
        this.runRepository = runRepository;
        this.runFactory = runFactory;
        this.windowCalculator = windowCalculator;
        this.domainEventPublisher = domainEventPublisher;
        this.transactionOperations = transactionOperations;
        this.clock = clock;
    }

    public AssignmentOrchestration orchestrate() {
        final List<DepartmentAssignmentConfig> configs = this.configServicePort.findConfigs();
        int triggered = 0;
        int disabled = 0;
        int activeRun = 0;
        int emptyWindow = 0;
        for (final DepartmentAssignmentConfig config : configs) {
            if (!config.enabled()) {
                disabled++;
                log.warn("Courier assignment of department {} skipped: automatic assignment is disabled",
                        config.departmentId().getValue());
                continue;
            }
            final DepartmentAssignmentDecision decision = decide(config);
            switch (decision) {
                case CREATED -> triggered++;
                case ACTIVE_RUN -> {
                    activeRun++;
                    log.warn("Courier assignment of department {} skipped: a run is already in progress",
                            config.departmentId().getValue());
                }
                case EMPTY_WINDOW -> emptyWindow++;
            }
        }
        log.info("Courier assignment orchestration triggered {} of {} departments (disabled={}, activeRun={}, "
                + "emptyWindow={})", triggered, configs.size(), disabled, activeRun, emptyWindow);
        return new AssignmentOrchestration(configs.size(), triggered, disabled, activeRun, emptyWindow);
    }

    private DepartmentAssignmentDecision decide(final DepartmentAssignmentConfig config) {
        try {
            return this.transactionOperations.execute(status -> {
                if (this.runRepository.existsActive(config.departmentId())) {
                    return DepartmentAssignmentDecision.ACTIVE_RUN;
                }
                final Instant now = this.clock.instant();
                final Optional<AssignmentWindow> window = this.windowCalculator.calculate(config,
                        this.runRepository.findLastWatermark(config.departmentId()), now);
                if (window.isEmpty()) {
                    return DepartmentAssignmentDecision.EMPTY_WINDOW;
                }
                final DepartmentAssignmentRun run = this.runFactory.create(config, window.get());
                this.runRepository.create(run);
                this.domainEventPublisher.publish(new DepartmentAssignmentRunCreated(run.getRunId(),
                        run.getDepartmentId(), now));
                return DepartmentAssignmentDecision.CREATED;
            });
        } catch (final RuntimeException exception) {
            log.warn("Courier assignment run of department {} was not created: {}",
                    config.departmentId().getValue(), exception.getMessage());
            return DepartmentAssignmentDecision.ACTIVE_RUN;
        }
    }

    private enum DepartmentAssignmentDecision {
        CREATED,
        ACTIVE_RUN,
        EMPTY_WINDOW
    }
}
