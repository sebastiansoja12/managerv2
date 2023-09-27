package com.warehouse.routetracker.domain.port.primary;

import java.util.*;


import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.service.RouteLoggerService;
import org.apache.commons.lang3.StringUtils;

import com.warehouse.routetracker.domain.vo.SupplyInformation;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLoggerService routeLoggerService;

    private static final String SYSTEM_USER = "system";


    @Override
    public void initializeRoute(Long parcelId) {
        final Route route = Route.builder()
                .parcelId(parcelId)
                .build();
        routeLoggerService.initializeRoute(route);
    }

    @Override
    public RouteResponse saveSupplyRoute(List<SupplyInformation> supplyInformation) {
		supplyInformation.stream().filter(this::existsToken).forEach(request -> {
			final Route route = Route.builder()
                    .supplierCode(request.getSupplierCode())
					.parcelId(request.getParcelId())
                    .depotCode(request.getDepotCode())
                    .build();
			// routeLogService.saveSupplyRoute(route);
		});

        return null;
    }

    @Override
    public List<RouteResponse> saveMultipleRoutes(List<RouteRequest> routeRequests) {

        final List<RouteResponse> responses = new ArrayList<>();
		routeRequests.forEach(routeRequest -> {
			routeRequest.getParcelIdList().forEach(parcelId -> {
				final Route route = Route.builder()
                        .parcelId(parcelId)
						.supplierCode(routeRequest.getSupplierCode())
                        .depotCode(routeRequest.getDepotCode())
						.username(routeRequest.getUsername())
                        .build();
                final List<ParcelStorage> parcelStorage = routeLoggerService.saveRoute(route);
                responses.add(mapParcelStorageToRouteResponse(parcelStorage));
			});
		});
      return responses;
    }

    private RouteResponse mapParcelStorageToRouteResponse(List<ParcelStorage> parcelStorage) {
        final ResponsibleUser responsibleUser = extractResponsibleUserFromMap(parcelStorage);
        final RegisteredParcel registeredParcel = extractRegisteredParcelFromMap(parcelStorage);
        return RouteResponse.builder()
                .responsibleUser(responsibleUser)
                .registeredParcels(registeredParcel)
                .build();
    }

    private RegisteredParcel extractRegisteredParcelFromMap(List<ParcelStorage> parcelStorage) {
        final List<Long> parcelIds = parcelStorage.stream()
                .map(ParcelStorage::getSavedParcelStatusMap)
                .flatMap(map -> map.keySet().stream())
                .distinct()
                .toList();
        return new RegisteredParcel(parcelIds);
    }


    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeLoggerService.deleteRoute(request);
    }

    private ResponsibleUser extractResponsibleUserFromMap(List<ParcelStorage> parcelStorage) {
        final String responsibleUser = parcelStorage.stream()
                .map(ParcelStorage::getUsername)
                .findAny()
                .orElse(SYSTEM_USER);
        return new ResponsibleUser(responsibleUser);
    }

    private boolean existsToken(SupplyInformation supplyInformation) {
        return Objects.nonNull(supplyInformation) && StringUtils.isNotEmpty(supplyInformation.getToken());
    }
}
