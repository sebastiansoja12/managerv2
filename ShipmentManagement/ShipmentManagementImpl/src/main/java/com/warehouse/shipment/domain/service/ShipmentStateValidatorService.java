package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentMetrics;

public interface ShipmentStateValidatorService {
    Result<Void, String> validateShipmentState(final Shipment shipment);

    Result<Void, String> validateShipmentLimitations(
            final OperatorShipmentConfiguration shipmentConfiguration, final ShipmentMetrics shipmentMetrics);
}
