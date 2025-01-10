package com.warehouse.routelogger.dto;

import java.util.List;

public record RequestDto(String request, List<ShipmentIdDto> shipmentIds, ProcessTypeDto processType) {}
