package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

import java.util.List;
import java.util.Optional;

public interface RouteLogRepository {

    RouteProcess save(final RouteLogRecord routeLogRecord);

    RouteLogRecord find(final ShipmentId shipmentId);

    Optional<RouteLogRecord> findById(final ShipmentId shipmentId);

    void update(final RouteLogRecord routeLogRecord);

    List<RouteLogRecord> findAll();
}
