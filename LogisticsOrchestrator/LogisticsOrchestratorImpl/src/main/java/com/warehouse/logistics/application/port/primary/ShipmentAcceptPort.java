package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.ShipmentAcceptCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptResult;

public interface ShipmentAcceptPort {

    ShipmentAcceptResult accept(final ShipmentAcceptCommand command);
}
