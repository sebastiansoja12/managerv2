package com.warehouse.routetracker.domain.port.secondary;

import java.util.List;
import java.util.Optional;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public interface RouteLogRepository {

    RouteProcess save(final RouteLogRecord routeLogRecord);

    RouteLogRecord find(final ShipmentId shipmentId);

    Optional<RouteLogRecord> findOptional(final ShipmentId shipmentId);

    void update(final RouteLogRecord routeLogRecord);

    List<RouteLogRecord> findAll();
}
