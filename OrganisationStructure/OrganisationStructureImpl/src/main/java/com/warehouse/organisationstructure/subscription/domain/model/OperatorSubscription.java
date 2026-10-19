package com.warehouse.organisationstructure.subscription.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.OperatorSubscriptionId;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionStatus;
import com.warehouse.organisationstructure.subscription.domain.exception.InvalidSubscriptionException;
import com.warehouse.organisationstructure.subscription.domain.vo.StarterSubscription;

import java.time.Instant;
import java.time.LocalDate;

public class OperatorSubscription {

    public static final String STARTER_UPDATED_BY = "system";

    private final OperatorSubscriptionId operatorSubscriptionId;

    private final OperatorId operatorId;

    private SubscriptionPlan plan;

    private LocalDate startDate;

    private LocalDate endDate;

    private String updatedBy;

    private Instant updatedAt;

    public OperatorSubscription(final OperatorSubscriptionId operatorSubscriptionId,
                                final OperatorId operatorId,
                                final SubscriptionPlan plan,
                                final LocalDate startDate,
                                final LocalDate endDate,
                                final String updatedBy,
                                final Instant updatedAt) {
        this.operatorSubscriptionId = operatorSubscriptionId;
        this.operatorId = operatorId;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public static OperatorSubscription assign(final OperatorId operatorId,
                                              final SubscriptionPlan plan,
                                              final LocalDate startDate,
                                              final LocalDate endDate,
                                              final String updatedBy,
                                              final Instant updatedAt) {
        validate(plan, startDate, endDate);
        return new OperatorSubscription(OperatorSubscriptionId.generate(), operatorId, plan, startDate, endDate, updatedBy, updatedAt);
    }

    public static OperatorSubscription starter(final OperatorId operatorId,
                                               final StarterSubscription starterSubscription,
                                               final LocalDate startDate,
                                               final Instant updatedAt) {
        return assign(operatorId, starterSubscription.plan(), startDate,
                startDate.plusDays(starterSubscription.durationDays() - 1L), STARTER_UPDATED_BY, updatedAt);
    }

    public void changePlan(final SubscriptionPlan plan,
                           final LocalDate startDate,
                           final LocalDate endDate,
                           final String updatedBy,
                           final Instant updatedAt) {
        validate(plan, startDate, endDate);
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public SubscriptionStatus statusOn(final LocalDate day) {
        if (day.isBefore(startDate)) {
            return SubscriptionStatus.NOT_STARTED;
        }
        if (day.isAfter(endDate)) {
            return SubscriptionStatus.EXPIRED;
        }
        return SubscriptionStatus.ACTIVE;
    }

    public boolean allowsAccessOn(final LocalDate day) {
        return SubscriptionStatus.ACTIVE.equals(statusOn(day));
    }

    public OperatorSubscriptionId getOperatorSubscriptionId() {
        return operatorSubscriptionId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public SubscriptionPlan getPlan() {
        return plan;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private static void validate(final SubscriptionPlan plan, final LocalDate startDate, final LocalDate endDate) {
        if (plan == null) {
            throw new InvalidSubscriptionException("Subscription plan is required");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidSubscriptionException("Subscription start date and end date are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidSubscriptionException("Subscription end date cannot be before its start date");
        }
    }
}
