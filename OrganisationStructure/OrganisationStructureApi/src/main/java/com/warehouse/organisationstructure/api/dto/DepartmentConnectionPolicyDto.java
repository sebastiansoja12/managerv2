package com.warehouse.organisationstructure.api.dto;

public record DepartmentConnectionPolicyDto(
        int sortingCenterConnectionCount,
        int maxAutoConnectionDistanceKm,
        boolean requireSortingConnectionForActivation,
        boolean bidirectionalConnectionsByDefault
) {
}
