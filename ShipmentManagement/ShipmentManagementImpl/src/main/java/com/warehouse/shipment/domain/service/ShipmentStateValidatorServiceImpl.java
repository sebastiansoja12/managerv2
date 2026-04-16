package com.warehouse.shipment.domain.service;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.ArrayList;
import java.util.List;

public class ShipmentStateValidatorServiceImpl implements ShipmentStateValidatorService {
	@Override
	public Result<Void, String> validateShipmentState(final Shipment shipment) {
		final List<String> validations = new ArrayList<>();
		if (shipment.isLocked()) {
			throw new RestException(400, "Cannot change shipment type when it is locked");
		}
		return Result.success();
	}
}
