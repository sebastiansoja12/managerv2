package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCodeApi;

public record ShipmentUpdateRequestApi(ShipmentIdDto shipmentId, PersonApi sender, PersonApi recipient,
									   DepartmentCodeApi destination,
									   DimensionsApi dimensions, WeightApi weight, String contentDescription,
									   MoneyApi declaredValue, String customerReference,
									   MoneyApi price, DangerousGoodApi dangerousGood,
									   ShipmentPriorityDto shipmentPriority, ShipmentStatusDto shipmentStatus, String issuerCountryCode,
									   String receiverCountryCode, ShipmentConfigurationApi shipmentConfiguration) {
}
