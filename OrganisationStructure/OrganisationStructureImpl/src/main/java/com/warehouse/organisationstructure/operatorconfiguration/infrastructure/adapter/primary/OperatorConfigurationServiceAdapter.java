package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;

import java.util.Optional;

public class OperatorConfigurationServiceAdapter implements OperatorConfigurationApiService {

    private final OperatorConfigurationPort operatorConfigurationPort;

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
    public Optional<ShipmentLimitsDto> getCurrentShipmentLimits() {
        return operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoCurrentShipmentLimits);
    }
}
