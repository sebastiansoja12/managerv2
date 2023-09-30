package com.warehouse.route.domain.port.primary;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.vo.DeliveryInformation;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogService routeLogService;

    @Override
    public void initializeRoute(Long parcelId) {
        final Route route = Route.builder()
                .parcelId(parcelId)
                .build();
        routeLogService.initializeRoute(route);
    }

    @Override
    public RouteResponse saveDelivery(List<DeliveryInformation> deliveryInformation) {
		deliveryInformation.stream().filter(this::existsToken).forEach(request -> {
			final Route route = Route.builder()
                    .supplierId(1L)
					.parcelId(request.getParcelId())
                    .depotCode(request.getDepotCode())
                    .build();
			 routeLogService.saveSupplyRoute(route);
		});

        return new RouteResponse(null);
    }

    @Override
    public RouteResponse saveRoute(RouteRequest routeRequest) {
        final Route route = Route.builder()
                .parcelId(routeRequest.getParcelId())
                .supplierId(routeRequest.getSupplierId())
                .depotCode(routeRequest.getDepotCode())
                .userId(routeRequest.getUserId())
                .build();
        return routeLogService.saveRoute(route);
    }

    @Override
    public void saveMultipleRoutes(List<RouteRequest> routeRequests) {
        routeRequests.stream()
                .map(this::mapToRoute)
                .forEach(routeLogService::saveRoute);
    }
    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeLogService.deleteRoute(request);
    }

    private Route mapToRoute(RouteRequest routeRequest) {
        return Route.builder()
                .parcelId(routeRequest.getParcelId())
                .supplierId(routeRequest.getSupplierId())
                .depotCode(routeRequest.getDepotCode())
                .userId(routeRequest.getUserId())
                .build();
    }

    private boolean existsToken(DeliveryInformation deliveryInformation) {
        return Objects.nonNull(deliveryInformation) && StringUtils.isNotEmpty(deliveryInformation.getToken());
    }
}
