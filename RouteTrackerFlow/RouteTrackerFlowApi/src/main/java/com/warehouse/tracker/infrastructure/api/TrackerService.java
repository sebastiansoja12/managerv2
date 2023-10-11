package com.warehouse.tracker.infrastructure.api;

import com.warehouse.routetracker.infrastructure.api.dto.RouteInformationDto;

import java.util.List;

public interface TrackerService<T> {

    List<RouteInformationDto> findByArg(T arg);
}
