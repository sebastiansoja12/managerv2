package com.warehouse.organisationstructure.subscription.application.port.primary.result;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionStatus;

import java.time.Instant;
import java.time.LocalDate;

public record OperatorSubscriptionResult(OperatorId operatorId,
                                         SubscriptionPlan plan,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         SubscriptionStatus status,
                                         String updatedBy,
                                         Instant updatedAt) {
}
