package com.warehouse.route.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.domain.port.secondary.RouteTrackerServicePort;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteMapper;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RouteLogAdapter implements RouteTrackerServicePort {

    private final RouteMapper routeMapper;

    private final RouteRepository routeRepository;

    private final AuthenticationPort authenticationPort;

    private final ShipmentPort shipmentPort;

    @Override
    public RouteResponse saveRoute(RouteRequest routeRequest) {
        final Route route = routeMapper.mapToRoute(routeRequest);
        return routeRepository.save(route);
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
        final String username = getUsername();
        final String depotCode = getDepotCode();
        routeRepository.deleteByParcelIdAndDepotCodeAndUsername(id, depotCode, username);
    }

    @Override
    public boolean exists(Long id) {
        return shipmentPort.loadParcel(id) != null;
    }

    public String getUsername() {
        return authenticationPort.findCurrentUser().get(0).getUsername();
    }

    public String getDepotCode() {
        return authenticationPort.findCurrentUser().get(0).getDepot().getDepotCode();
    }
}
