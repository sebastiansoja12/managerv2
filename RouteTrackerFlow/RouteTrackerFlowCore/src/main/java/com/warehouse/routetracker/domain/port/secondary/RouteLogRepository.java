package com.warehouse.routetracker.domain.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteProcess;

public interface RouteLogRepository {

    RouteProcess save(final RouteLogRecord routeLogRecord);

    RouteLogRecord find(final ShipmentId shipmentId);

    void update(final RouteLogRecord routeLogRecord);

    List<RouteLogRecord> findAll();
}
