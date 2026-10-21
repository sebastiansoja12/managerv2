package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.mapper;

import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.entity.OperatorSubscriptionEntity;

public final class OperatorSubscriptionEntityMapper {

    private OperatorSubscriptionEntityMapper() {
    }

    public static OperatorSubscriptionEntity toEntity(final OperatorSubscription operatorSubscription) {
        final OperatorSubscriptionEntity entity = new OperatorSubscriptionEntity(
                operatorSubscription.getOperatorSubscriptionId(), operatorSubscription.getPlan(),
                operatorSubscription.getStartDate(), operatorSubscription.getEndDate(),
                operatorSubscription.getUpdatedBy(), operatorSubscription.getUpdatedAt());
        entity.assignOperator(operatorSubscription.getOperatorId());
        return entity;
    }

    public static OperatorSubscription toModel(final OperatorSubscriptionEntity entity) {
        return new OperatorSubscription(entity.getOperatorSubscriptionId(), entity.operatorId(), entity.getPlan(),
                entity.getStartDate(), entity.getEndDate(), entity.getUpdatedBy(), entity.getUpdatedAt());
    }
}
