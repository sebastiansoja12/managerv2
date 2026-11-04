package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Objects;
import java.util.stream.Stream;

public record DepartmentConfigurationOverride(DepartmentId departmentId,
                                              Boolean autoAssignDeliveryAreas,
                                              Boolean autoConnectDepartments,
                                              Boolean preventDepartmentDeletion,
                                              Boolean preventDepartmentArchiving,
                                              DeliveryAreaMode deliveryAreaMode,
                                              Integer maxDeliveryRadiusKm,
                                              Boolean allowManualDeliveryAreaEdit,
                                              Boolean recalculateAreasOnStatusChange,
                                              UncoveredPostalCodePolicy uncoveredPostalCodePolicy,
                                              Integer sortingCenterConnectionCount,
                                              Integer maxAutoConnectionDistanceKm,
                                              Boolean requireSortingConnectionForActivation,
                                              Boolean bidirectionalConnectionsByDefault,
                                              Boolean requireStatusChangeReason,
                                              Boolean blockDeactivationWithActiveWork,
                                              DepartmentWorkTransferMode workTransferOnDeactivation,
                                              Integer autoArchiveInactiveAfterDays,
                                              Integer dailyCapacity,
                                              Integer capacityWarningThresholdPercent,
                                              Boolean redirectOverflowToNeighbour,
                                              Integer maxCouriersPerDepartment) {

    public DepartmentConfigurationOverride {
        Objects.requireNonNull(departmentId, "Department ID cannot be null");
    }

    public static DepartmentConfigurationOverride empty(final DepartmentId departmentId) {
        return new DepartmentConfigurationOverride(departmentId, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public boolean isEmpty() {
        return Stream.of(autoAssignDeliveryAreas, autoConnectDepartments, preventDepartmentDeletion,
                        preventDepartmentArchiving, deliveryAreaMode, maxDeliveryRadiusKm, allowManualDeliveryAreaEdit,
                        recalculateAreasOnStatusChange, uncoveredPostalCodePolicy, sortingCenterConnectionCount,
                        maxAutoConnectionDistanceKm, requireSortingConnectionForActivation,
                        bidirectionalConnectionsByDefault, requireStatusChangeReason, blockDeactivationWithActiveWork,
                        workTransferOnDeactivation, autoArchiveInactiveAfterDays, dailyCapacity,
                        capacityWarningThresholdPercent, redirectOverflowToNeighbour, maxCouriersPerDepartment)
                .allMatch(Objects::isNull);
    }

    public DepartmentConfiguration applyTo(final DepartmentConfiguration defaults) {
        final DepartmentDeliveryAreaPolicy deliveryArea = defaults.getDeliveryAreaPolicy();
        final DepartmentConnectionPolicy connection = defaults.getConnectionPolicy();
        final DepartmentLifecyclePolicy lifecycle = defaults.getLifecyclePolicy();
        final DepartmentCapacityPolicy capacity = defaults.getCapacityPolicy();
        return new DepartmentConfiguration(
                valueOr(autoAssignDeliveryAreas, defaults.isAutoAssignDeliveryAreas()),
                valueOr(autoConnectDepartments, defaults.isAutoConnectDepartments()),
                valueOr(preventDepartmentDeletion, defaults.isPreventDepartmentDeletion()),
                valueOr(preventDepartmentArchiving, defaults.isPreventDepartmentArchiving()),
                new DepartmentDeliveryAreaPolicy(
                        valueOr(deliveryAreaMode, deliveryArea.getDeliveryAreaMode()),
                        valueOr(maxDeliveryRadiusKm, deliveryArea.getMaxDeliveryRadiusKm()),
                        valueOr(allowManualDeliveryAreaEdit, deliveryArea.isAllowManualDeliveryAreaEdit()),
                        valueOr(recalculateAreasOnStatusChange, deliveryArea.isRecalculateAreasOnStatusChange()),
                        valueOr(uncoveredPostalCodePolicy, deliveryArea.getUncoveredPostalCodePolicy())),
                new DepartmentConnectionPolicy(
                        valueOr(sortingCenterConnectionCount, connection.getSortingCenterConnectionCount()),
                        valueOr(maxAutoConnectionDistanceKm, connection.getMaxAutoConnectionDistanceKm()),
                        valueOr(requireSortingConnectionForActivation,
                                connection.isRequireSortingConnectionForActivation()),
                        valueOr(bidirectionalConnectionsByDefault, connection.isBidirectionalConnectionsByDefault())),
                new DepartmentLifecyclePolicy(
                        valueOr(requireStatusChangeReason, lifecycle.isRequireStatusChangeReason()),
                        valueOr(blockDeactivationWithActiveWork, lifecycle.isBlockDeactivationWithActiveWork()),
                        valueOr(workTransferOnDeactivation, lifecycle.getWorkTransferOnDeactivation()),
                        valueOr(autoArchiveInactiveAfterDays, lifecycle.getAutoArchiveInactiveAfterDays())),
                new DepartmentCapacityPolicy(
                        valueOr(dailyCapacity, capacity.getDefaultDailyCapacity()),
                        valueOr(capacityWarningThresholdPercent, capacity.getCapacityWarningThresholdPercent()),
                        valueOr(redirectOverflowToNeighbour, capacity.isRedirectOverflowToNeighbour()),
                        valueOr(maxCouriersPerDepartment, capacity.getMaxCouriersPerDepartment())));
    }

    private static <T> T valueOr(final T value, final T fallback) {
        return value != null ? value : fallback;
    }
}
