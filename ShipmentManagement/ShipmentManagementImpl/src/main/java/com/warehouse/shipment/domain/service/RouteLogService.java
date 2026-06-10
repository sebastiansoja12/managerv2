package com.warehouse.shipment.domain.service;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.RouteLogRecord;

public interface RouteLogService {

    Optional<RouteLogRecord> findByShipmentId(final ShipmentId shipmentId);
}
