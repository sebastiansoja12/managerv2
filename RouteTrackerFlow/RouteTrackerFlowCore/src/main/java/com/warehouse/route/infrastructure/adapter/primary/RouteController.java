package com.warehouse.route.infrastructure.adapter.primary;

import com.warehouse.route.domain.model.*;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.route.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.route.infrastructure.api.dto.RouteRequestDto;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteTrackerLogPort trackerLogPort;

    private final RouteRequestMapper requestMapper = Mappers.getMapper(RouteRequestMapper.class);

    private final RouteResponseMapper responseMapper = Mappers.getMapper(RouteResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> saveMultipleRoutes(@RequestBody List<RouteRequestDto> routeRequests) {
        final List<RouteRequest> requests = requestMapper.map(routeRequests);
        final List<RouteResponse> responses = trackerLogPort.saveRoutes(requests);
        return new ResponseEntity<>(responseMapper.mapToResponse(responses), HttpStatus.OK);
    }

    @GetMapping("/by-parcel/{parcelId}")
    public ResponseEntity<?> getRouteListByParcelId(@PathVariable Long parcelId) {
        final List<Routes> routes = trackerLogPort.getRouteListByParcelId(parcelId);
        return new ResponseEntity<>(responseMapper.map(routes), HttpStatus.FOUND);
    }

    @GetMapping("/by-username/{username}")
    public List<Routes> findAllByUsername(@PathVariable String username) {
        return new ArrayList<>();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteByParcelId(@RequestBody RouteDeleteRequest request) {
        trackerLogPort.deleteRoute(request);
        return ResponseEntity.status(OK).body("Recorded route for given parcel has been deleted");
    }
}
