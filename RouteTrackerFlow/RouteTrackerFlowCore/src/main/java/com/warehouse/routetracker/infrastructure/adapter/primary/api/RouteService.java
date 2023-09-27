package com.warehouse.routetracker.infrastructure.adapter.primary.api;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.RouteRequestDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.RouteResponseDto;

public interface RouteService {

    RouteResponseDto save(RouteRequestDto routeRequestDto);
}
