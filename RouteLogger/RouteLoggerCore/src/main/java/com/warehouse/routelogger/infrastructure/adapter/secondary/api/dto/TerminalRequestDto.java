package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import com.warehouse.routelogger.dto.ShipmentIdDto;


public record TerminalRequestDto(String request, ShipmentIdDto shipmentId, String processType) {
}
