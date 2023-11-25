package com.warehouse.routetracker.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteRepositoryImpl implements RouteRepository {

    private final RouteReadRepository routeReadRepository;

    private final RouteModelMapper mapper;


    @Override
    public List<RouteInformation> findByParcelId(Long parcelId) {

        final List<RouteEntity> routeEntity = routeReadRepository.findByParcelId(parcelId);

        return mapper.mapToRoutes(routeEntity);
    }

    @Override
    public RouteResponse save(RouteLogRecord routeLogRecord) {
        final RouteEntity routeEntity = mapper.map(routeLogRecord);

        routeReadRepository.save(routeEntity);

        return mapper.mapToRouteResponse(routeEntity);
    }

    @Override
    public List<RouteInformation> findByUsername(String username) {
        return mapper.mapToRoutes(routeReadRepository.findAllByUserUsername(username));
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeReadRepository.deleteById(request.getId());
    }
}
