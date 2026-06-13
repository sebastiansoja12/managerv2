package com.warehouse.shipment.domain.service;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentLimits;
import com.warehouse.shipment.domain.vo.conf.ShipmentMetrics;

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

	@Override
	public Result<Void, String> validateShipmentLimitations(final OperatorShipmentConfiguration shipmentConfiguration,
															final ShipmentMetrics shipmentMetrics) {
		final ShipmentLimits shipmentLimits = shipmentConfiguration.limits();

		if (!shipmentLimits.allowOversized()) {
            return shipmentMetrics.validateBasedOnLimits(shipmentLimits);
		}

		return Result.success();
	}
}
