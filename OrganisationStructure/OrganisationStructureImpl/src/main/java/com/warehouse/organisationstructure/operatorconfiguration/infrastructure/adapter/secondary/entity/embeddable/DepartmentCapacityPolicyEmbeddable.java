package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentCapacityPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DepartmentCapacityPolicyEmbeddable {

    @Column(name = "department_default_daily_capacity")
    private int defaultDailyCapacity;

    @Column(name = "department_capacity_warning_threshold_percent")
    private int capacityWarningThresholdPercent;

    @Column(name = "department_redirect_overflow_to_neighbour")
    private boolean redirectOverflowToNeighbour;

    @Column(name = "department_max_couriers_per_department")
    private int maxCouriersPerDepartment;

    public DepartmentCapacityPolicyEmbeddable() {
    }

    public static DepartmentCapacityPolicyEmbeddable from(final DepartmentCapacityPolicy configuration) {
        final DepartmentCapacityPolicy source = configuration != null
                ? configuration
                : DepartmentCapacityPolicy.defaultConfiguration();
        final DepartmentCapacityPolicyEmbeddable embeddable = new DepartmentCapacityPolicyEmbeddable();
        embeddable.defaultDailyCapacity = source.getDefaultDailyCapacity();
        embeddable.capacityWarningThresholdPercent = source.getCapacityWarningThresholdPercent();
        embeddable.redirectOverflowToNeighbour = source.isRedirectOverflowToNeighbour();
        embeddable.maxCouriersPerDepartment = source.getMaxCouriersPerDepartment();
        return embeddable;
    }

    public DepartmentCapacityPolicy toModel() {
        return new DepartmentCapacityPolicy(
                defaultDailyCapacity,
                capacityWarningThresholdPercent,
                redirectOverflowToNeighbour,
                maxCouriersPerDepartment
        );
    }
}
