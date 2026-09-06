package com.warehouse.organisationstructure.subscription.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignStarterSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.result.OperatorSubscriptionResult;
import com.warehouse.organisationstructure.subscription.application.port.secondary.CurrentOperatorServicePort;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorServicePort;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorSubscriptionRepository;
import com.warehouse.organisationstructure.subscription.application.port.secondary.PlatformActorServicePort;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.exception.OperatorNotFoundException;
import com.warehouse.organisationstructure.subscription.domain.exception.PlatformOwnerRequiredException;
import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;
import com.warehouse.organisationstructure.subscription.domain.vo.PlatformActor;
import com.warehouse.organisationstructure.subscription.domain.vo.StarterSubscription;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
public class OperatorSubscriptionPortImpl implements OperatorSubscriptionPort {

    private final OperatorSubscriptionRepository operatorSubscriptionRepository;

    private final OperatorServicePort operatorServicePort;

    private final PlatformActorServicePort platformActorServicePort;

    private final CurrentOperatorServicePort currentOperatorServicePort;

    private final StarterSubscription starterSubscription;

    private final Clock clock;

    public OperatorSubscriptionPortImpl(final OperatorSubscriptionRepository operatorSubscriptionRepository,
                                        final OperatorServicePort operatorServicePort,
                                        final PlatformActorServicePort platformActorServicePort,
                                        final CurrentOperatorServicePort currentOperatorServicePort,
                                        final StarterSubscription starterSubscription,
                                        final Clock clock) {
        this.operatorSubscriptionRepository = operatorSubscriptionRepository;
        this.operatorServicePort = operatorServicePort;
        this.platformActorServicePort = platformActorServicePort;
        this.currentOperatorServicePort = currentOperatorServicePort;
        this.starterSubscription = starterSubscription;
        this.clock = clock;
    }

    @Override
    public Optional<OperatorSubscriptionResult> find(final OperatorId operatorId) {
        requireExistingOperator(operatorId);
        return operatorSubscriptionRepository.find(operatorId).map(this::toResult);
    }

    @Override
    public OperatorSubscriptionResult assign(final AssignSubscriptionCommand command) {
        final PlatformActor actor = platformActorServicePort.currentActor()
                .filter(currentActor -> !currentActor.impersonation())
                .orElseThrow(PlatformOwnerRequiredException::new);
        requireExistingOperator(command.operatorId());
        final Instant now = Instant.now(clock);
        return operatorSubscriptionRepository.find(command.operatorId())
                .map(current -> changePlan(current, command, actor, now))
                .orElseGet(() -> assignFirstPlan(command, actor, now));
    }

    private OperatorSubscriptionResult changePlan(final OperatorSubscription operatorSubscription,
                                                  final AssignSubscriptionCommand command,
                                                  final PlatformActor actor,
                                                  final Instant now) {
        final SubscriptionPlan previousPlan = operatorSubscription.getPlan();
        final LocalDate previousStartDate = operatorSubscription.getStartDate();
        final LocalDate previousEndDate = operatorSubscription.getEndDate();
        operatorSubscription.changePlan(command.plan(), command.startDate(), command.endDate(), actor.username(), now);
        final OperatorSubscription saved = operatorSubscriptionRepository.save(operatorSubscription);
        log.info("Subscription of operator {} changed by platform owner {} from plan {} valid from {} to {} to plan {} "
                        + "valid from {} to {}", command.operatorId(), actor.username(), previousPlan,
                previousStartDate, previousEndDate, saved.getPlan(), saved.getStartDate(), saved.getEndDate());
        return toResult(saved);
    }

    private OperatorSubscriptionResult assignFirstPlan(final AssignSubscriptionCommand command,
                                                       final PlatformActor actor,
                                                       final Instant now) {
        final OperatorSubscription saved = operatorSubscriptionRepository.save(OperatorSubscription.assign(
                command.operatorId(), command.plan(), command.startDate(), command.endDate(), actor.username(), now));
        log.info("Subscription plan {} valid from {} to {} assigned to operator {} by platform owner {}",
                saved.getPlan(), saved.getStartDate(), saved.getEndDate(), command.operatorId(),
                actor.username());
        return toResult(saved);
    }

    @Override
    public Optional<OperatorSubscriptionResult> findCurrent() {
        return currentOperatorServicePort.currentOperatorId()
                .flatMap(operatorSubscriptionRepository::find)
                .map(this::toResult);
    }

    @Override
    public boolean isCurrentAccessAllowed() {
        if (platformActorServicePort.isPlatformActorRequest()) {
            return true;
        }
        return currentOperatorServicePort.currentOperatorId()
                .map(operatorId -> operatorSubscriptionRepository.find(operatorId)
                        .map(operatorSubscription -> operatorSubscription.allowsAccessOn(today()))
                        .orElse(false))
                .orElse(true);
    }

    @Override
    public void assignStarter(final AssignStarterSubscriptionCommand command) {
        operatorSubscriptionRepository.find(command.operatorId()).ifPresentOrElse(
                existing -> log.info("Starter subscription skipped for operator {} because plan {} is already assigned",
                        command.operatorId(), existing.getPlan()),
                () -> {
                    final OperatorSubscription operatorSubscription = operatorSubscriptionRepository.save(
                            OperatorSubscription.starter(command.operatorId(), starterSubscription,
                                    LocalDate.ofInstant(command.operatorCreatedAt(), clock.getZone()),
                                    Instant.now(clock)));
                    log.info("Starter subscription plan {} valid from {} to {} assigned to operator {}",
                            operatorSubscription.getPlan(), operatorSubscription.getStartDate(),
                            operatorSubscription.getEndDate(), command.operatorId());
                });
    }

    private void requireExistingOperator(final OperatorId operatorId) {
        if (!operatorServicePort.exists(operatorId)) {
            throw new OperatorNotFoundException(operatorId);
        }
    }

    private OperatorSubscriptionResult toResult(final OperatorSubscription operatorSubscription) {
        return new OperatorSubscriptionResult(operatorSubscription.getOperatorId(), operatorSubscription.getPlan(),
                operatorSubscription.getStartDate(), operatorSubscription.getEndDate(),
                operatorSubscription.statusOn(today()), operatorSubscription.getUpdatedBy(),
                operatorSubscription.getUpdatedAt());
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}
