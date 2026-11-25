package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.PricedShipment;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;

public interface ShipmentPricingServicePort {

    Result<PricedShipment, ErrorCode> quote(final ShipmentCreateCommand command,
                                            final ShipmentServiceLevel serviceLevel);
}
