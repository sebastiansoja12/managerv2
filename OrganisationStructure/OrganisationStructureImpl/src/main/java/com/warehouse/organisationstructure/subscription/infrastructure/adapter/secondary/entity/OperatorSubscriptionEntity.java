package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorSubscriptionId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "subscription.OperatorSubscriptionEntity")
@Table(name = "operator_subscriptions")
public class OperatorSubscriptionEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "subscription_id", nullable = false))
    private OperatorSubscriptionId operatorSubscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 32)
    private SubscriptionPlan plan;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected OperatorSubscriptionEntity() {
    }

    public OperatorSubscriptionEntity(final OperatorSubscriptionId operatorSubscriptionId,
                                      final SubscriptionPlan plan,
                                      final LocalDate startDate,
                                      final LocalDate endDate,
                                      final String updatedBy,
                                      final Instant updatedAt) {
        this.operatorSubscriptionId = operatorSubscriptionId;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public OperatorSubscriptionId getOperatorSubscriptionId() {
        return operatorSubscriptionId;
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
}
