package com.warehouse.organisationstructure.subscription;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.secondary.CurrentOperatorServicePort;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorServicePort;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorSubscriptionRepository;
import com.warehouse.organisationstructure.subscription.application.port.secondary.PlatformActorServicePort;
import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;
import com.warehouse.organisationstructure.subscription.domain.vo.PlatformActor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InMemoryOperatorSubscriptionStore implements OperatorSubscriptionRepository, OperatorServicePort,
        PlatformActorServicePort, CurrentOperatorServicePort {

    private final Map<OperatorId, OperatorSubscription> subscriptions = new HashMap<>();

    private final Set<OperatorId> existingOperators;

    private PlatformActor platformActor;

    private OperatorId currentOperatorId;

    public InMemoryOperatorSubscriptionStore(final Set<OperatorId> existingOperators) {
        this.existingOperators = existingOperators;
    }

    public void actAsPlatformActor(final PlatformActor actor) {
        this.platformActor = actor;
    }

    public void actAsOperator(final OperatorId operatorId) {
        this.currentOperatorId = operatorId;
    }

    @Override
    public Optional<OperatorSubscription> find(final OperatorId operatorId) {
        return Optional.ofNullable(subscriptions.get(operatorId));
    }

    @Override
    public OperatorSubscription save(final OperatorSubscription operatorSubscription) {
        subscriptions.put(operatorSubscription.getOperatorId(), operatorSubscription);
        return operatorSubscription;
    }

    @Override
    public boolean exists(final OperatorId operatorId) {
        return existingOperators.contains(operatorId);
    }

    @Override
    public Optional<PlatformActor> currentActor() {
        return Optional.ofNullable(platformActor);
    }

    @Override
    public boolean isPlatformActorRequest() {
        return platformActor != null;
    }

    @Override
    public Optional<OperatorId> currentOperatorId() {
        return Optional.ofNullable(currentOperatorId);
    }
}
