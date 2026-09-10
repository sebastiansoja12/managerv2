package com.warehouse.organisationstructure.subscription.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;

import java.util.Optional;

public interface OperatorSubscriptionRepository {

    Optional<OperatorSubscription> find(final OperatorId operatorId);

    OperatorSubscription save(final OperatorSubscription operatorSubscription);
}
