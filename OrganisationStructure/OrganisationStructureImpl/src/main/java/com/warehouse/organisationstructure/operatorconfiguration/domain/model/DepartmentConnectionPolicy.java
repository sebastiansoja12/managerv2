package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DepartmentConnectionPolicy {

    private final int sortingCenterConnectionCount;
    private final int maxAutoConnectionDistanceKm;
    private final boolean requireSortingConnectionForActivation;
    private final boolean bidirectionalConnectionsByDefault;

    public DepartmentConnectionPolicy(final int sortingCenterConnectionCount,
                                      final int maxAutoConnectionDistanceKm,
                                      final boolean requireSortingConnectionForActivation,
                                      final boolean bidirectionalConnectionsByDefault) {
        this.sortingCenterConnectionCount = sortingCenterConnectionCount;
        this.maxAutoConnectionDistanceKm = maxAutoConnectionDistanceKm;
        this.requireSortingConnectionForActivation = requireSortingConnectionForActivation;
        this.bidirectionalConnectionsByDefault = bidirectionalConnectionsByDefault;
    }

    public static DepartmentConnectionPolicy defaultConfiguration() {
        return new DepartmentConnectionPolicy(
                1,
                300,
                false,
                true
        );
    }

    public int getSortingCenterConnectionCount() { return sortingCenterConnectionCount; }
    public int getMaxAutoConnectionDistanceKm() { return maxAutoConnectionDistanceKm; }
    public boolean isRequireSortingConnectionForActivation() { return requireSortingConnectionForActivation; }
    public boolean isBidirectionalConnectionsByDefault() { return bidirectionalConnectionsByDefault; }
}
