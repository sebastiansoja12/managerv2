package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfigurationOverride;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.DepartmentConfigurationOverrideEntity;

public class DepartmentConfigurationOverrideMapper {

    public DepartmentConfigurationOverrideEntity toEntity(final DepartmentConfigurationOverride override) {
        return new DepartmentConfigurationOverrideEntity(override.departmentId(), override.autoAssignDeliveryAreas(),
                override.autoConnectDepartments(), override.preventDepartmentDeletion(),
                override.preventDepartmentArchiving(), override.deliveryAreaMode(), override.maxDeliveryRadiusKm(),
                override.allowManualDeliveryAreaEdit(), override.recalculateAreasOnStatusChange(),
                override.uncoveredPostalCodePolicy(), override.sortingCenterConnectionCount(),
                override.maxAutoConnectionDistanceKm(), override.requireSortingConnectionForActivation(),
                override.bidirectionalConnectionsByDefault(), override.requireStatusChangeReason(),
                override.blockDeactivationWithActiveWork(), override.workTransferOnDeactivation(),
                override.autoArchiveInactiveAfterDays(), override.dailyCapacity(),
                override.capacityWarningThresholdPercent(), override.redirectOverflowToNeighbour(),
                override.maxCouriersPerDepartment());
    }

    public DepartmentConfigurationOverride toDomain(final DepartmentConfigurationOverrideEntity entity) {
        return new DepartmentConfigurationOverride(entity.getDepartmentId(), entity.getAutoAssignDeliveryAreas(),
                entity.getAutoConnectDepartments(), entity.getPreventDepartmentDeletion(),
                entity.getPreventDepartmentArchiving(), entity.getDeliveryAreaMode(), entity.getMaxDeliveryRadiusKm(),
                entity.getAllowManualDeliveryAreaEdit(), entity.getRecalculateAreasOnStatusChange(),
                entity.getUncoveredPostalCodePolicy(), entity.getSortingCenterConnectionCount(),
                entity.getMaxAutoConnectionDistanceKm(), entity.getRequireSortingConnectionForActivation(),
                entity.getBidirectionalConnectionsByDefault(), entity.getRequireStatusChangeReason(),
                entity.getBlockDeactivationWithActiveWork(), entity.getWorkTransferOnDeactivation(),
                entity.getAutoArchiveInactiveAfterDays(), entity.getDailyCapacity(),
                entity.getCapacityWarningThresholdPercent(), entity.getRedirectOverflowToNeighbour(),
                entity.getMaxCouriersPerDepartment());
    }
}
