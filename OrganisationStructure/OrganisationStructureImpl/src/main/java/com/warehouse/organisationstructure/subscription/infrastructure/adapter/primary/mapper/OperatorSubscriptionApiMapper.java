package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.result.OperatorSubscriptionResult;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.exception.InvalidSubscriptionException;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.AssignSubscriptionApiRequest;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.CurrentSubscriptionApiResponse;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.OperatorSubscriptionApiResponse;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public final class OperatorSubscriptionApiMapper {

    public static final String NOT_ASSIGNED_STATUS = "NOT_ASSIGNED";

    private OperatorSubscriptionApiMapper() {
    }

    public static AssignSubscriptionCommand toCommand(final OperatorId operatorId,
                                                      final AssignSubscriptionApiRequest request) {
        return new AssignSubscriptionCommand(operatorId, toPlan(request.plan()), request.startDate(),
                request.endDate());
    }

    public static OperatorSubscriptionApiResponse toResponse(final OperatorSubscriptionResult result) {
        return new OperatorSubscriptionApiResponse(result.operatorId(), result.plan().name(),
                result.startDate(), result.endDate(), result.status().name(), result.updatedBy(), result.updatedAt());
    }

    public static CurrentSubscriptionApiResponse toCurrentResponse(final Optional<OperatorSubscriptionResult> result,
                                                                   final boolean accessAllowed) {
        return result
                .map(subscription -> new CurrentSubscriptionApiResponse(subscription.plan().name(),
                        subscription.startDate(), subscription.endDate(), subscription.status().name(), accessAllowed))
                .orElseGet(() -> new CurrentSubscriptionApiResponse(null, null, null, NOT_ASSIGNED_STATUS,
                        accessAllowed));
    }

    private static SubscriptionPlan toPlan(final String plan) {
        final String normalizedPlan = plan.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(SubscriptionPlan.values())
                .filter(subscriptionPlan -> subscriptionPlan.name().equals(normalizedPlan))
                .findFirst()
                .orElseThrow(() -> new InvalidSubscriptionException("Unknown subscription plan: " + plan));
    }
}
