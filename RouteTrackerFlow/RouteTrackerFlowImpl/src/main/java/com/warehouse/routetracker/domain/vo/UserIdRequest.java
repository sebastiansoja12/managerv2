package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public record UserIdRequest(UserId userId, ShipmentId shipmentId, ProcessType processType) {
}
