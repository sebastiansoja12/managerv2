package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberPrefixModeDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.application.port.secondary.CurrentDepartmentCodePort;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OperatorShipmentConfigurationMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentConfigurationServiceAdapter implements ShipmentConfigurationPort {

    private final OperatorConfigurationApiService operatorConfigurationApiService;
    private final OperatorShipmentConfigurationMapper mapper;
    private final CurrentDepartmentCodePort currentDepartmentCodePort;

    public ShipmentConfigurationServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService,
                                               final OperatorShipmentConfigurationMapper mapper,
                                               final CurrentDepartmentCodePort currentDepartmentCodePort) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
        this.mapper = mapper;
        this.currentDepartmentCodePort = currentDepartmentCodePort;
    }

    @Override
    public OperatorShipmentConfiguration getCurrentOperatorShipmentConfiguration() {
        final ShipmentConfigurationDto shipmentConfiguration =
                operatorConfigurationApiService.getCurrentShipmentConfiguration();
        return mapper.map(shipmentConfiguration, currentDepartmentCode(shipmentConfiguration));
    }

    private DepartmentCode currentDepartmentCode(final ShipmentConfigurationDto configuration) {
        if (configuration == null
                || configuration.trackingNumberRule() == null
                || configuration.trackingNumberRule().prefixMode() != TrackingNumberPrefixModeDto.DEPARTMENT_CODE) {
            return null;
        }
        return currentDepartmentCodePort.currentDepartmentCode();
    }
}
