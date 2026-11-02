package com.warehouse.organisationstructure.api.dto;

public record DepartmentDeliveryAreaPolicyDto(
        DeliveryAreaModeDto deliveryAreaMode,
        int maxDeliveryRadiusKm,
        boolean allowManualDeliveryAreaEdit,
        boolean recalculateAreasOnStatusChange,
        UncoveredPostalCodePolicyDto uncoveredPostalCodePolicy
) {
}
