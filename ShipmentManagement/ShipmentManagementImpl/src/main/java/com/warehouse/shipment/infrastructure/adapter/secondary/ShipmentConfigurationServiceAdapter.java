package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.shipment.domain.port.secondary.ShipmentConfigurationServicePort;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OperatorShipmentConfigurationMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentConfigurationServiceAdapter implements ShipmentConfigurationServicePort {

    private final OperatorConfigurationApiService operatorConfigurationApiService;
    private final OperatorShipmentConfigurationMapper mapper;

    public ShipmentConfigurationServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService) {
        this(operatorConfigurationApiService, new OperatorShipmentConfigurationMapper());
    }

    public ShipmentConfigurationServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService,
                                               final OperatorShipmentConfigurationMapper mapper) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
        this.mapper = mapper;
    }

    @Override
    public OperatorShipmentConfiguration getCurrentOperatorShipmentConfiguration() {
        final ShipmentConfigurationDto shipmentConfiguration =
                operatorConfigurationApiService.getCurrentShipmentConfiguration();
        return mapper.map(shipmentConfiguration);
    }
}
