package com.warehouse.deliverynetwork.domain.vo;

public record DepartmentConnectionRules(boolean autoConnectDepartments,
                                        int sortingFacilityConnectionCount,
                                        int maxAutoConnectionDistanceInKilometers,
                                        boolean requireSortingConnectionForActivation,
                                        boolean bidirectionalByDefault) {

    public static DepartmentConnectionRules defaultRules() {
        return new DepartmentConnectionRules(false, 1, 300, false, true);
    }
}
