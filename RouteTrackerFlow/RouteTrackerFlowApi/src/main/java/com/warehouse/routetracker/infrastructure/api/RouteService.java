package com.warehouse.routetracker.infrastructure.api;

import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteResponseDto;

public interface RouteService {

    RouteResponseDto save(RouteRequestDto routeRequestDto);
}
