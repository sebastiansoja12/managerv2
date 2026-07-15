package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.identifier.SupplierId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public record SupplierIdRequest(SupplierId supplierId, ShipmentId shipmentId, ProcessType processType) {
}
