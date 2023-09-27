package com.warehouse.routetracker.infrastructure.adapter.secondary;

import com.warehouse.routetracker.domain.model.Route;
import com.warehouse.routetracker.domain.model.RouteDeleteRequest;
import com.warehouse.routetracker.domain.model.RouteResponse;
import com.warehouse.routetracker.domain.model.Routes;
import com.warehouse.routetracker.domain.port.secondary.RouteTrackerRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class RouteTrackerRepositoryImpl implements RouteTrackerRepository {

    private final RouteTrackerReadRepository routeTrackerReadRepository;

    private final RouteModelMapper mapper;


    @Override
    public List<Routes> findByParcelId(Long parcelId) {

        final List<RouteEntity> routeEntity = null;

        return mapper.mapToRoutes(routeEntity);
    }

    @Override
    public void initializeRoute(Route route) {
        final RouteEntity routeEntity = mapper.mapInitialize(route);
        routeTrackerReadRepository.save(routeEntity);
    }

    @Override
    public RouteResponse saveSupplyRoute(Route route) {
        final RouteEntity routeEntity = mapper.map(route);



        routeTrackerReadRepository.save(routeEntity);

        return mapper.mapToRouteResponse(routeEntity);
    }

    @Override
    public RouteEntity save(Route route) {
        final RouteEntity routeEntity = mapper.map(route);
        routeEntity.setCreated(LocalDateTime.now());
        return routeTrackerReadRepository.insert(routeEntity);
    }

    @Override
    public List<Routes> findByUsername(String username) {
        return new ArrayList<>();
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
    }
}
