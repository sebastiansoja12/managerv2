package com.warehouse.routetracker.domain.port.secondary;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteProcess;

public interface RouteLogRepository {

    RouteProcess save(RouteLogRecord routeLogRecord);

    RouteLogRecord find(Long parcelId);

    void update(RouteLogRecord routeLogRecord);

    List<RouteLogRecord> findAll();
}
