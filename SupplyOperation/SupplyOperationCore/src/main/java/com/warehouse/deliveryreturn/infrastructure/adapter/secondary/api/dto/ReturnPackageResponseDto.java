package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import com.warehouse.deliveryreturn.infrastructure.api.dto.ReturnTokenDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ShipmentIdDto;

public record ReturnPackageResponseDto(ReturnTokenDto returnToken, ShipmentIdDto shipmentId) {
}
