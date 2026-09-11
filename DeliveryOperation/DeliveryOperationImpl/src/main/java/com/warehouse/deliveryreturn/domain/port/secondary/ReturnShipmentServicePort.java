package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.domain.vo.ShipmentReturnResult;

public interface ReturnShipmentServicePort {

    ShipmentReturnResult returnShipment(final ProcessId processId, final ShipmentId shipmentId);
}
