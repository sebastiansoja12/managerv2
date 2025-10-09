package com.warehouse.returning.domain.port.secondary;

import java.util.List;

import com.warehouse.returning.domain.enumeration.ErrorCode;
import com.warehouse.returning.domain.enumeration.ResponseStatus;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.vo.ShipmentId;

public interface ShipmentValidatorServicePort {
    Result<ResponseStatus, ErrorCode> validateShipments(final List<ShipmentId> shipmentId);
}
