package com.warehouse.route.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.route.domain.model.*;
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
    public void saveDelivery(List<DeliveryInformation> deliveryInformation) {
		deliveryInformation.stream().filter(this::existsToken).forEach(request -> {
			final Route route = Route.builder()
                    .supplierId(1L)
					.parcelId(request.getParcelId())
                    .depotCode(request.getDepotCode())
                    .build();
			 routeLogService.saveSupplyRoute(route);
		});
    }

    @Override
    public List<RouteResponse> saveRoutes(List<RouteRequest> routeRequests) {
		return routeRequests.stream()
                .map(this::mapToRoute)
                .map(routeLogService::saveRoute)
				.collect(Collectors.toList());
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeLogService.deleteRoute(request);
    }

    @Override
    public List<Routes> getRouteListByParcelId(Long parcelId) {
        return routeLogService.getRouteListByParcelId(parcelId);
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
