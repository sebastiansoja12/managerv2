package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;

public interface ShipmentConfigurationPort {

    OperatorShipmentConfiguration getCurrentOperatorShipmentConfiguration();
}
