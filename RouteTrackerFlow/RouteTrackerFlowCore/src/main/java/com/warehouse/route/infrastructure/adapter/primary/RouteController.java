package com.warehouse.route.infrastructure.adapter.primary;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.sun.mail.iap.Response.OK;

@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteTrackerLogPort trackerLogPort;

    @PostMapping
    public RouteResponse saveRoute(@RequestBody RouteRequest routeRequest) {
        return trackerLogPort.saveRoute(routeRequest);
    }

    @PostMapping("/multiple")
    public void saveMultipleRoutes(@RequestBody List<RouteRequest> routeRequests) {
         trackerLogPort.saveMultipleRoutes(routeRequests);
    }

    @GetMapping("/by-parcel/{parcelId}")
    public List<Routes> getRouteListByParcelId(@PathVariable Long parcelId) {
        return trackerLogPort.findByParcelId(parcelId);
    }

    @GetMapping("/by-username/{username}")
    public List<Routes> findAllByUsername(@PathVariable String username) {
        return trackerLogPort.findByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteByParcelId(@Valid @PathVariable Long id) {
        trackerLogPort.deleteRoute(id);
        return ResponseEntity.status(OK).body("Recorded route for given parcel has been deleted");
    }
}
