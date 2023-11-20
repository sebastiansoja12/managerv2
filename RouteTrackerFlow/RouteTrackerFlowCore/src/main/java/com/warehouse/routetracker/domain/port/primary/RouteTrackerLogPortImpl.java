package com.warehouse.routetracker.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.warehouse.routetracker.domain.port.secondary.ParcelStatusUpdateRepository;
import com.warehouse.routetracker.domain.vo.*;
import org.apache.commons.lang3.StringUtils;

import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteRepository repository;

    private final ParcelStatusUpdateRepository updateRepository;

    @Override
    public void initializeRoute(Long parcelId) {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .parcelId(parcelId)
                .build();
        repository.save(routeLogRecord);
    }

    @Override
    public void saveDelivery(List<DeliveryInformation> deliveryInformation) {
		deliveryInformation.stream().filter(this::existsToken).forEach(request -> {
			final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                    .supplierCode(request.getSupplierCode())
					.parcelId(request.getParcelId())
                    .depotCode(request.getDepotCode())
                    .status(request.getDeliveryStatus())
                    .build();
            updateParcelStatus(routeLogRecord);
            repository.save(routeLogRecord);
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

    private void updateParcelStatus(RouteLogRecord routeLogRecord) {
        updateRepository.updateStatus(routeLogRecord.getParcelId(), routeLogRecord.getStatus());
    }

    private RouteLogRecord mapToRoute(RouteRequest routeRequest) {
        return RouteLogRecord.builder()
                .parcelId(routeRequest.getParcelId())
                .supplierCode(routeRequest.getSupplierCode())
                .depotCode(routeRequest.getDepotCode())
                .username(routeRequest.getUsername())
                .build();
    }

    private boolean existsToken(DeliveryInformation deliveryInformation) {
        return Objects.nonNull(deliveryInformation) && StringUtils.isNotEmpty(deliveryInformation.getToken());
    }
}
