package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.api.dto.DeliveryAreaModeDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationOverrideDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationsDto;
import com.warehouse.organisationstructure.api.dto.DepartmentWorkTransferModeDto;
import com.warehouse.organisationstructure.api.dto.UncoveredPostalCodePolicyDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfigurationOverride;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfigurations;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentWorkTransferMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UncoveredPostalCodePolicy;

import java.util.stream.Collectors;

public class DepartmentConfigurationOverrideApiMapper {

    public DepartmentConfigurationOverrideDto toDto(final DepartmentConfigurationOverride override) {
        return new DepartmentConfigurationOverrideDto(override.autoAssignDeliveryAreas(),
                override.autoConnectDepartments(), override.preventDepartmentDeletion(),
                override.preventDepartmentArchiving(),
                new DepartmentConfigurationOverrideDto.DeliveryAreaPolicy(
                        override.deliveryAreaMode() == null ? null
                                : DeliveryAreaModeDto.valueOf(override.deliveryAreaMode().name()),
                        override.maxDeliveryRadiusKm(), override.allowManualDeliveryAreaEdit(),
                        override.recalculateAreasOnStatusChange(),
                        override.uncoveredPostalCodePolicy() == null ? null
                                : UncoveredPostalCodePolicyDto.valueOf(override.uncoveredPostalCodePolicy().name())),
                new DepartmentConfigurationOverrideDto.ConnectionPolicy(override.sortingCenterConnectionCount(),
                        override.maxAutoConnectionDistanceKm(), override.requireSortingConnectionForActivation(),
                        override.bidirectionalConnectionsByDefault()),
                new DepartmentConfigurationOverrideDto.LifecyclePolicy(override.requireStatusChangeReason(),
                        override.blockDeactivationWithActiveWork(),
                        override.workTransferOnDeactivation() == null ? null
                                : DepartmentWorkTransferModeDto.valueOf(override.workTransferOnDeactivation().name()),
                        override.autoArchiveInactiveAfterDays()),
                new DepartmentConfigurationOverrideDto.CapacityPolicy(override.dailyCapacity(),
                        override.capacityWarningThresholdPercent(), override.redirectOverflowToNeighbour(),
                        override.maxCouriersPerDepartment()));
    }

    public DepartmentConfigurationOverride toModel(final DepartmentId departmentId,
                                                   final DepartmentConfigurationOverrideDto dto) {
        final DepartmentConfigurationOverrideDto.DeliveryAreaPolicy deliveryArea = dto.deliveryAreaPolicy() != null
                ? dto.deliveryAreaPolicy()
                : new DepartmentConfigurationOverrideDto.DeliveryAreaPolicy(null, null, null, null, null);
        final DepartmentConfigurationOverrideDto.ConnectionPolicy connection = dto.connectionPolicy() != null
                ? dto.connectionPolicy()
                : new DepartmentConfigurationOverrideDto.ConnectionPolicy(null, null, null, null);
        final DepartmentConfigurationOverrideDto.LifecyclePolicy lifecycle = dto.lifecyclePolicy() != null
                ? dto.lifecyclePolicy()
                : new DepartmentConfigurationOverrideDto.LifecyclePolicy(null, null, null, null);
        final DepartmentConfigurationOverrideDto.CapacityPolicy capacity = dto.capacityPolicy() != null
                ? dto.capacityPolicy()
                : new DepartmentConfigurationOverrideDto.CapacityPolicy(null, null, null, null);
        return new DepartmentConfigurationOverride(departmentId, dto.autoAssignDeliveryAreas(),
                dto.autoConnectDepartments(), dto.preventDepartmentDeletion(), dto.preventDepartmentArchiving(),
                deliveryArea.deliveryAreaMode() == null ? null
                        : DeliveryAreaMode.valueOf(deliveryArea.deliveryAreaMode().name()),
                deliveryArea.maxDeliveryRadiusKm(), deliveryArea.allowManualDeliveryAreaEdit(),
                deliveryArea.recalculateAreasOnStatusChange(),
                deliveryArea.uncoveredPostalCodePolicy() == null ? null
                        : UncoveredPostalCodePolicy.valueOf(deliveryArea.uncoveredPostalCodePolicy().name()),
                connection.sortingCenterConnectionCount(), connection.maxAutoConnectionDistanceKm(),
                connection.requireSortingConnectionForActivation(), connection.bidirectionalConnectionsByDefault(),
                lifecycle.requireStatusChangeReason(), lifecycle.blockDeactivationWithActiveWork(),
                lifecycle.workTransferOnDeactivation() == null ? null
                        : DepartmentWorkTransferMode.valueOf(lifecycle.workTransferOnDeactivation().name()),
                lifecycle.autoArchiveInactiveAfterDays(), capacity.defaultDailyCapacity(),
                capacity.capacityWarningThresholdPercent(), capacity.redirectOverflowToNeighbour(),
                capacity.maxCouriersPerDepartment());
    }

    public DepartmentConfigurationsDto toDto(final DepartmentConfigurations configurations) {
        return new DepartmentConfigurationsDto(
                OperatorMapper.toDtoDepartmentConfiguration(configurations.defaults()),
                configurations.departments().entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().getValue(),
                                entry -> OperatorMapper.toDtoDepartmentConfiguration(entry.getValue()))));
    }
}
