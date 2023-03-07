package com.warehouse.tracker.infrastructure.api;

import com.warehouse.tracker.infrastructure.api.dto.RoutesDto;

import java.util.List;

public interface TrackerService<T> {

    List<RoutesDto> findByArg(T arg);
}
