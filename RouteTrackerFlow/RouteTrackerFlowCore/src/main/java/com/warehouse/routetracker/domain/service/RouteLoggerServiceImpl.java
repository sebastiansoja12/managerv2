package com.warehouse.routetracker.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.port.secondary.RouteTrackerRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLoggerServiceImpl implements RouteLoggerService {

    private final RouteTrackerRepository routeTrackerRepository;

    private final String STATUS = "OK";

    private final String FAILURE_REASON = "OK";

    @Override
    public void initializeRoute(Route route) {
        routeTrackerRepository.initializeRoute(route);
    }

    @Override
    public RouteResponse saveSupplyRoute(Route route) {
        return routeTrackerRepository.saveSupplyRoute(route);
    }

    @Override
    public List<ParcelStorage> saveRoute(Route route) {
        final Map<Long, MessageResponse> savedParcelStatusMap = new HashMap<>();
        final List<ParcelStorage> parcelStorages = new ArrayList<>();
        final RouteEntity routeEntity = routeTrackerRepository.save(route);
        savedParcelStatusMap.put(routeEntity.getParcelId(), new MessageResponse(
                routeEntity.getId(), STATUS, FAILURE_REASON));
        parcelStorages.add(new ParcelStorage(savedParcelStatusMap, route.getUsername()));
        return parcelStorages;
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeTrackerRepository.deleteRoute(request);
    }
}
