package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationsDto;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.mapper.DepartmentConfigurationOverrideApiMapper;
import com.warehouse.auth.OperatorCapabilityService;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShippingCapabilities;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;

import java.util.Optional;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.FleetConfigurationDto;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.RegionalConfigurationDto;
import com.warehouse.organisationstructure.api.dto.SortingConfigurationDto;
import com.warehouse.organisationstructure.api.dto.PickupPointConfigurationDto;

public class OperatorConfigurationServiceAdapter implements OperatorConfigurationApiService, OperatorCapabilityService {

    private final OperatorConfigurationPort operatorConfigurationPort;

    private final DepartmentConfigurationOverrideApiMapper departmentConfigurationOverrideApiMapper =
            new DepartmentConfigurationOverrideApiMapper();

    public OperatorConfigurationServiceAdapter(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @Override
    public Optional<OperatorConfigurationDto> getCurrent() {
        return operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoConfiguration);
    }

    @Override
    public ShipmentConfigurationDto getCurrentShipmentConfiguration() {
        return operatorConfigurationPort.getCurrent()
                .map(configuration -> OperatorMapper.toDtoShipmentConfiguration(
                        configuration.getShipmentConfiguration()))
                .orElse(null);
    }

    @Override
    public ShipmentConfigurationDto updateCurrentShipmentConfiguration(final ShipmentConfigurationDto configuration) {
        return OperatorMapper.toDtoShipmentConfiguration(operatorConfigurationPort
                .updateCurrentShipmentConfiguration(OperatorMapper.toModelShipmentConfiguration(configuration))
                .getShipmentConfiguration());
    }

    @Override
    public Optional<ShipmentLimitsDto> getCurrentShipmentLimits() {
        return operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoCurrentShipmentLimits);
    }

    @Override
    public ShipmentNotificationConfigurationDto getShipmentNotificationConfiguration(final OperatorId operatorId) {
        return OperatorMapper.mapShipmentNotificationConfiguration(
                operatorConfigurationPort.getShipmentNotificationConfiguration(operatorId));
    }

    @Override
    public boolean supportsReturnShipments() {
        return operatorConfigurationPort.getCurrent()
                .map(OperatorConfiguration::getShippingCapabilities)
                .map(ShippingCapabilities::isSupportsReturnShipments)
                .orElse(false);
    }

    @Override
    public DepartmentConfigurationDto getCurrentDepartmentConfiguration() {
        return OperatorMapper.toDtoDepartmentConfiguration(operatorConfigurationPort.getCurrentDepartmentConfiguration());
    }

    @Override
    public DepartmentConfigurationDto updateCurrentDepartmentConfiguration(final DepartmentConfigurationDto configuration) {
        return OperatorMapper.toDtoDepartmentConfiguration(operatorConfigurationPort.updateCurrentDepartmentConfiguration(
                OperatorMapper.toModelDepartmentConfiguration(configuration)));
    }

    @Override
    public DepartmentConfigurationDto getDepartmentConfiguration(final DepartmentId departmentId) {
        return OperatorMapper.toDtoDepartmentConfiguration(operatorConfigurationPort.getDepartmentConfiguration(departmentId));
    }

    @Override
    public DepartmentConfigurationsDto getDepartmentConfigurations() {
        return departmentConfigurationOverrideApiMapper.toDto(operatorConfigurationPort.getDepartmentConfigurations());
    }

    @Override
    public FleetConfigurationDto getCurrentFleetConfiguration() {
        return OperatorMapper.toDtoFleetConfiguration(operatorConfigurationPort.getCurrentFleetConfiguration());
    }

    @Override
    public FleetConfigurationDto updateCurrentFleetConfiguration(final FleetConfigurationDto configuration) {
        return OperatorMapper.toDtoFleetConfiguration(operatorConfigurationPort.updateCurrentFleetConfiguration(
                OperatorMapper.toModelFleetConfiguration(configuration)));
    }

    @Override
    public CommunicationConfigurationDto getCurrentCommunicationConfiguration() {
        return OperatorMapper.toDtoCommunicationConfiguration(operatorConfigurationPort.getCurrentCommunicationConfiguration());
    }

    @Override
    public CommunicationConfigurationDto updateCurrentCommunicationConfiguration(final CommunicationConfigurationDto configuration) {
        return OperatorMapper.toDtoCommunicationConfiguration(operatorConfigurationPort.updateCurrentCommunicationConfiguration(
                OperatorMapper.toModelCommunicationConfiguration(configuration)));
    }

    @Override
    public RegionalConfigurationDto getCurrentRegionalConfiguration() {
        return OperatorMapper.toDtoRegionalConfiguration(operatorConfigurationPort.getCurrentRegionalConfiguration());
    }

    @Override
    public RegionalConfigurationDto updateCurrentRegionalConfiguration(final RegionalConfigurationDto configuration) {
        return OperatorMapper.toDtoRegionalConfiguration(operatorConfigurationPort.updateCurrentRegionalConfiguration(
                OperatorMapper.toModelRegionalConfiguration(configuration)));
    }

    @Override
    public SortingConfigurationDto getCurrentSortingConfiguration() {
        return OperatorMapper.toDtoSortingConfiguration(operatorConfigurationPort.getCurrentSortingConfiguration());
    }

    @Override
    public SortingConfigurationDto updateCurrentSortingConfiguration(final SortingConfigurationDto configuration) {
        return OperatorMapper.toDtoSortingConfiguration(operatorConfigurationPort.updateCurrentSortingConfiguration(
                OperatorMapper.toModelSortingConfiguration(configuration)));
    }

    @Override
    public PickupPointConfigurationDto getCurrentPickupPointConfiguration() {
        return OperatorMapper.toDtoPickupPointConfiguration(operatorConfigurationPort.getCurrentPickupPointConfiguration());
    }

    @Override
    public PickupPointConfigurationDto updateCurrentPickupPointConfiguration(final PickupPointConfigurationDto configuration) {
        return OperatorMapper.toDtoPickupPointConfiguration(operatorConfigurationPort.updateCurrentPickupPointConfiguration(
                OperatorMapper.toModelPickupPointConfiguration(configuration)));
    }
}
