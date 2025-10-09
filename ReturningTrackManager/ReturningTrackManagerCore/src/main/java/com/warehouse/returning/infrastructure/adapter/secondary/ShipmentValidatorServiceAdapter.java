package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.domain.enumeration.ErrorCode;
import com.warehouse.returning.domain.enumeration.ResponseStatus;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.port.secondary.ShipmentValidatorServicePort;
import com.warehouse.returning.domain.vo.ShipmentId;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ShipmentValidatorServiceAdapter implements ShipmentValidatorServicePort {

    @Override
    public Result<ResponseStatus, ErrorCode> validateShipments(final List<ShipmentId> shipmentId) {
        return null;
    }
}
