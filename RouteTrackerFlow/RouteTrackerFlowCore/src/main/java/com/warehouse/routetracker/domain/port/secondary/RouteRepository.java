package com.warehouse.routetracker.domain.port.secondary;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteResponse;

public interface RouteRepository {

    RouteResponse save(RouteLogRecord routeLogRecord);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteInformation> findByParcelId(Long parcelId);

    List<RouteInformation> findByUsername(String username);
}
