package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import org.springframework.stereotype.Service;

// TODO
@Service
public class AddressValidationService {
    public Result<Void, ErrorCode> validateAddresses(final Address sender, final Address recipient,
                                                     final ShipmentValidationRules shipmentValidationRules) {
        return Result.success();
    }
}
