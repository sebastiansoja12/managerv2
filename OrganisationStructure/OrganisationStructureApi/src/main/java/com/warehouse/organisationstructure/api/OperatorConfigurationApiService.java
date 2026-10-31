package com.warehouse.organisationstructure.api;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;

import java.util.Optional;

public interface OperatorConfigurationApiService {

    Optional<OperatorConfigurationDto> getCurrent();

    ShipmentConfigurationDto getCurrentShipmentConfiguration();

    ShipmentConfigurationDto updateCurrentShipmentConfiguration(final ShipmentConfigurationDto configuration);

    Optional<ShipmentLimitsDto> getCurrentShipmentLimits();

    ShipmentNotificationConfigurationDto getShipmentNotificationConfiguration(final OperatorId operatorId);
}
