package com.warehouse.route.infrastructure.adapter.secondary;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.RouteEntity;
import com.warehouse.route.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteModelMapper;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class RouteRepositoryImpl implements RouteRepository {

    private final RouteReadRepository routeReadRepository;

    private final RouteDepotReadRepository depotReadRepository;

    private final RouteModelMapper mapper;


    @Override
    public List<Routes> findByParcelId(Long parcelId) {

        final List<RouteEntity> routeEntity = routeReadRepository.findByParcelId(parcelId);

        return mapper.mapToRoutes(routeEntity);
    }

    @Override
    public void initializeRoute(Route route) {
        final RouteEntity routeEntity = mapper.map(route);
        routeReadRepository.saveAndFlush(routeEntity);
    }

    @Override
    public RouteResponse saveSupplyRoute(Route route) {
        final RouteEntity routeEntity = mapper.map(route);

        final ParcelEntity parcelEntity = routeEntity.getParcel();
        parcelEntity.setStatus(Status.SENT);

        routeEntity.setParcel(parcelEntity);

        routeReadRepository.save(routeEntity);

        return mapper.mapToRouteResponse(routeEntity);
    }

    @Override
    public RouteResponse save(Route route) {
        final RouteEntity routeEntity = mapper.map(route);

        routeReadRepository.save(routeEntity);

        return mapper.mapToRouteResponse(routeEntity);
    }

    @Override
    public List<Routes> findByUsername(String username) {
        return mapper.mapToRoutes(routeReadRepository.findAllByUserUsername(username));
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeReadRepository.deleteById(request.getId());
    }
}
