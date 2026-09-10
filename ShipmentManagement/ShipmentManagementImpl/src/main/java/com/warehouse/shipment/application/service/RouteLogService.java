package com.warehouse.shipment.application.service;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.RouteLogRecord;

public interface RouteLogService {

    Optional<RouteLogRecord> findByShipmentId(final ShipmentId shipmentId);
}
