package com.warehouse.route.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.domain.port.secondary.RouteTrackerServicePort;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteMapper;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.dto.RouteResponseDto;
import com.warehouse.route.infrastructure.api.event.RouteResponseEvent;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO to be deleted
@AllArgsConstructor
public class RouteLogAdapter implements RouteTrackerServicePort {

    private final RouteMapper routeMapper;

    private final RouteRepository routeRepository;

    private final AuthenticationPort authenticationPort;

    private final ShipmentPort shipmentPort;

    private final RouteLogEventPublisher eventPublisher;

    @Override
    public void saveRoute(RouteRequest routeRequest) {
        final Route route = routeMapper.mapToRoute(routeRequest);
        final RouteResponse response =  routeRepository.save(route);
        final RouteResponseDto routeResponseDto = routeMapper.map(response);
        eventPublisher.send(new RouteResponseEvent(routeResponseDto));
    }

    @Override
    public List<Routes> findByParcelId(Long parcelId) {
        return routeRepository.findByParcelId(parcelId);
    }

    @Override
    public List<Routes> findByUsername(String username) {
        return routeRepository.findByUsername(username).stream()
                .sorted(Comparator.comparing(Routes::getCreated).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Method obtains from AuthSecurityCore module information
     * about current logged in user and sends it to repository to
     * delete given route
     * @param id
     */
    @Override
    public void deleteRoute(Long id) {
        // TODO change to RouteDeleteRequest
        routeRepository.deleteByParcelIdAndDepotCodeAndUsername(id, "", "");
    }

    @Override
    public boolean exists(Long id) {
        return Objects.nonNull(shipmentPort.loadParcel(id));
    }
}
