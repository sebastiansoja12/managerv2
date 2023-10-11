package com.warehouse.routetracker.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.warehouse.routetracker.domain.port.secondary.ParcelStatusUpdateRepository;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import org.apache.commons.lang3.StringUtils;

import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.DeliveryInformation;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteRepository repository;

    private final ParcelStatusUpdateRepository updateRepository;

    @Override
    public void initializeRoute(Long parcelId) {
        final Route route = Route.builder()
                .parcelId(parcelId)
                .build();
        repository.initializeRoute(route);
    }

    @Override
    public void saveDelivery(List<DeliveryInformation> deliveryInformation) {
		deliveryInformation.stream().filter(this::existsToken).forEach(request -> {
			final Route route = Route.builder()
                    .supplierCode(request.getSupplierCode())
					.parcelId(request.getParcelId())
                    .depotCode(request.getDepotCode())
                    .build();
            final SupplyRoute supplyRoute = SupplyRoute.builder()
                    .route(route)
                    .status(request.getDeliveryStatus())
                    .build();
            updateParcelStatus(supplyRoute);
            repository.saveSupplyRoute(supplyRoute);
		});
    }

    @Override
    public List<RouteResponse> saveRoutes(List<RouteRequest> routeRequests) {
		return routeRequests.stream()
                .map(this::mapToRoute)
                .map(repository::save)
				.collect(Collectors.toList());
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        repository.deleteRoute(request);
    }

    @Override
    public List<RouteInformation> getRouteListByParcelId(Long parcelId) {
        return repository.findByParcelId(parcelId);
    }

    @Override
    public List<RouteInformation> findRoutesByUsername(String username) {
        return repository.findByUsername(username);
    }

    private void updateParcelStatus(SupplyRoute supplyRoute) {
        updateRepository.updateStatus(supplyRoute.getRoute().getParcelId(), supplyRoute.getStatus());
    }

    private Route mapToRoute(RouteRequest routeRequest) {
        return Route.builder()
                .parcelId(routeRequest.getParcelId())
                .supplierCode(routeRequest.getSupplierCode())
                .depotCode(routeRequest.getDepotCode())
                .userId(routeRequest.getUserId())
                .build();
    }

    private boolean existsToken(DeliveryInformation deliveryInformation) {
        return Objects.nonNull(deliveryInformation) && StringUtils.isNotEmpty(deliveryInformation.getToken());
    }
}
