package com.warehouse.organisationstructure.api;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;

import java.util.Optional;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.FleetConfigurationDto;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.RegionalConfigurationDto;
import com.warehouse.organisationstructure.api.dto.SortingConfigurationDto;
import com.warehouse.organisationstructure.api.dto.PickupPointConfigurationDto;

public interface OperatorConfigurationApiService {

    Optional<OperatorConfigurationDto> getCurrent();

    ShipmentConfigurationDto getCurrentShipmentConfiguration();

    ShipmentConfigurationDto updateCurrentShipmentConfiguration(final ShipmentConfigurationDto configuration);

    Optional<ShipmentLimitsDto> getCurrentShipmentLimits();

    ShipmentNotificationConfigurationDto getShipmentNotificationConfiguration(final OperatorId operatorId);

    DepartmentConfigurationDto getCurrentDepartmentConfiguration();

    DepartmentConfigurationDto updateCurrentDepartmentConfiguration(final DepartmentConfigurationDto configuration);

    FleetConfigurationDto getCurrentFleetConfiguration();

    FleetConfigurationDto updateCurrentFleetConfiguration(final FleetConfigurationDto configuration);

    CommunicationConfigurationDto getCurrentCommunicationConfiguration();

    CommunicationConfigurationDto updateCurrentCommunicationConfiguration(final CommunicationConfigurationDto configuration);

    RegionalConfigurationDto getCurrentRegionalConfiguration();

    RegionalConfigurationDto updateCurrentRegionalConfiguration(final RegionalConfigurationDto configuration);

    SortingConfigurationDto getCurrentSortingConfiguration();

    SortingConfigurationDto updateCurrentSortingConfiguration(final SortingConfigurationDto configuration);

    PickupPointConfigurationDto getCurrentPickupPointConfiguration();

    PickupPointConfigurationDto updateCurrentPickupPointConfiguration(final PickupPointConfigurationDto configuration);
}
