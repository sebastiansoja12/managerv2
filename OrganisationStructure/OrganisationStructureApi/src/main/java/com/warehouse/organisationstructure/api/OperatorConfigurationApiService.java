package com.warehouse.organisationstructure.api;

import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;

import java.util.Optional;

public interface OperatorConfigurationApiService {

    Optional<OperatorConfigurationDto> getCurrent();

    ShipmentConfigurationDto getCurrentShipmentConfiguration();

    Optional<ShipmentLimitsDto> getCurrentShipmentLimits();
}
