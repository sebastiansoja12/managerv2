package com.warehouse.routetracker.infrastructure.adapter.primary;

import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.RouteRequestDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.routetracker.domain.model.RouteDeleteRequest;
import com.warehouse.routetracker.domain.model.RouteRequest;
import com.warehouse.routetracker.domain.model.RouteResponse;
import com.warehouse.routetracker.domain.model.Routes;
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
public class RouteTrackerController {

    private final RouteTrackerLogPort trackerLogPort;

    private final RouteRequestMapper routeRequestMapper = Mappers.getMapper(RouteRequestMapper.class);

    private final RouteResponseMapper routeResponseMapper = Mappers.getMapper(RouteResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> save(@RequestBody List<RouteRequestDto> routeRequests) {
        final List<RouteRequest> requests = routeRequestMapper.map(routeRequests);
        final List<RouteResponse> responses = trackerLogPort.saveMultipleRoutes(requests);
        return new ResponseEntity<>(routeResponseMapper.map(responses), HttpStatus.OK);
    }

    @GetMapping("/by-parcel/{parcelId}")
    public List<Routes> getRouteListByParcelId(@PathVariable Long parcelId) {
        return new ArrayList<>();
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
