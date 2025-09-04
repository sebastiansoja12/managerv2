package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.ReturnModel;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ReturningServicePort {
    Result<ReturnModel, ErrorCode> notifyShipmentReturn(final ShipmentSnapshot snapshot);
}
