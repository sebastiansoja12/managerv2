package com.warehouse.route.infrastructure.adapter.primary;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.sun.mail.iap.Response.OK;

@RestController
@RequestMapping("/v2/api/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteTrackerLogPort trackerLogPort;


    @PostMapping
    public RouteResponse saveRoute(@RequestBody RouteRequest routeRequest) {
        return trackerLogPort.saveRoute(routeRequest);
    }

    @GetMapping("/by-parcel/{parcelId}")
    public List<Route> getRouteListByParcelId(@PathVariable Long parcelId) {
        return trackerLogPort.findByParcelId(parcelId);
    }

    @GetMapping("/by-username/{username}")
    public List<Route> findAllByUsername(@PathVariable String username) {
        return trackerLogPort.findByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteByParcelId(@Valid @PathVariable Long id) {
        trackerLogPort.deleteRoute(id);
        return ResponseEntity.status(OK).body("Recorded route for given parcel has been deleted");
    }



}
