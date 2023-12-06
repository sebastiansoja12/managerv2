package com.warehouse.routetracker.infrastructure.adapter.primary;

import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.routetracker.infrastructure.api.dto.ParcelIdDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteTrackerController {

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
        final List<RouteInformation> routes = trackerLogPort.getRouteListByParcelId(parcelId);
        return new ResponseEntity<>(responseMapper.map(routes), HttpStatus.FOUND);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> findAllByUsername(@PathVariable String username) {
        final List<RouteInformation> routes = trackerLogPort.findRoutesByUsername(username);
        return new ResponseEntity<>(responseMapper.map(routes), HttpStatus.FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRouteByParcelId(@RequestBody RouteDeleteRequest request) {
        trackerLogPort.deleteRoute(request);
        return ResponseEntity.status(OK).body("Recorded route for given parcel has been deleted");
    }
}
