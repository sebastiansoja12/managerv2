package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;

public interface ShipmentConfigurationServicePort {

    OperatorShipmentConfiguration getCurrentOperatorShipmentConfiguration();
}
