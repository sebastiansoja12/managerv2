package com.warehouse.organisationstructure.api.dto;

public record DepartmentConfigurationOverrideDto(Boolean autoAssignDeliveryAreas,
                                                 Boolean autoConnectDepartments,
                                                 Boolean preventDepartmentDeletion,
                                                 Boolean preventDepartmentArchiving,
                                                 DeliveryAreaPolicy deliveryAreaPolicy,
                                                 ConnectionPolicy connectionPolicy,
                                                 LifecyclePolicy lifecyclePolicy,
                                                 CapacityPolicy capacityPolicy) {

    public record DeliveryAreaPolicy(DeliveryAreaModeDto deliveryAreaMode,
                                     Integer maxDeliveryRadiusKm,
                                     Boolean allowManualDeliveryAreaEdit,
                                     Boolean recalculateAreasOnStatusChange,
                                     UncoveredPostalCodePolicyDto uncoveredPostalCodePolicy) {
    }

    public record ConnectionPolicy(Integer sortingCenterConnectionCount,
                                   Integer maxAutoConnectionDistanceKm,
                                   Boolean requireSortingConnectionForActivation,
                                   Boolean bidirectionalConnectionsByDefault) {
    }

    public record LifecyclePolicy(Boolean requireStatusChangeReason,
                                  Boolean blockDeactivationWithActiveWork,
                                  DepartmentWorkTransferModeDto workTransferOnDeactivation,
                                  Integer autoArchiveInactiveAfterDays) {
    }

    public record CapacityPolicy(Integer defaultDailyCapacity,
                                 Integer capacityWarningThresholdPercent,
                                 Boolean redirectOverflowToNeighbour,
                                 Integer maxCouriersPerDepartment) {
    }
}
