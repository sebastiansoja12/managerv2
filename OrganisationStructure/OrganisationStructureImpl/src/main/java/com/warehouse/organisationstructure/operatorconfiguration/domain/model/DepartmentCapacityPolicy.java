package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DepartmentCapacityPolicy {

    private final int defaultDailyCapacity;
    private final int capacityWarningThresholdPercent;
    private final boolean redirectOverflowToNeighbour;
    private final int maxCouriersPerDepartment;

    public DepartmentCapacityPolicy(final int defaultDailyCapacity,
                                    final int capacityWarningThresholdPercent,
                                    final boolean redirectOverflowToNeighbour,
                                    final int maxCouriersPerDepartment) {
        this.defaultDailyCapacity = defaultDailyCapacity;
        this.capacityWarningThresholdPercent = capacityWarningThresholdPercent;
        this.redirectOverflowToNeighbour = redirectOverflowToNeighbour;
        this.maxCouriersPerDepartment = maxCouriersPerDepartment;
    }

    public static DepartmentCapacityPolicy defaultConfiguration() {
        return new DepartmentCapacityPolicy(
                1000,
                85,
                false,
                0
        );
    }

    public int getDefaultDailyCapacity() { return defaultDailyCapacity; }
    public int getCapacityWarningThresholdPercent() { return capacityWarningThresholdPercent; }
    public boolean isRedirectOverflowToNeighbour() { return redirectOverflowToNeighbour; }
    public int getMaxCouriersPerDepartment() { return maxCouriersPerDepartment; }
}
