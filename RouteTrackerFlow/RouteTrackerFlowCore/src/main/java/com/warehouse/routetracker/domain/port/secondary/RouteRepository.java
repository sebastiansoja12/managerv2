package com.warehouse.routetracker.domain.port.secondary;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.domain.vo.RouteResponse;

public interface RouteRepository {

    RouteResponse save(RouteLogRecord routeLogRecord);

    RouteProcess save(RouteLogRecordToChange routeLogRecordToChange);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteInformation> findByParcelId(Long parcelId);

    List<RouteInformation> findByUsername(String username);

    RouteLogRecordToChange find(Long parcelId);

    void update(RouteLogRecordToChange routeLogRecord);

    List<RouteLogRecordToChange> findAll();
}
