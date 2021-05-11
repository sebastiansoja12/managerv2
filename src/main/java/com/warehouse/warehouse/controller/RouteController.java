package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.model.Route;
import com.warehouse.warehouse.model.User;
import com.warehouse.warehouse.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public Route saveRoute(@RequestBody Route route){
        return routeService.save(route);
    }

    @GetMapping("/all/allUsers")
    public List<Route> findAllRoutes(){
        List<Route> allRoutes = routeService.findAllRoutes();
        return allRoutes.stream()
                .sorted(Comparator.comparing(Route::getCreated).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<Route> findAllByUsername(){
        List<Route> routesByUsername = routeService.findAllByUsername();
      return  routesByUsername.stream()
                .sorted(Comparator.comparing(Route::getCreated).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @GetMapping("/all/parcelId/{id}")
    public ResponseEntity<List<Route>> findByParcelCode(@PathVariable UUID id) throws Exception {
        List<Route> route =  routeService.findByParcelId(id);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }
    @PostMapping("/all/parcelId/{id}")
    public ResponseEntity<String> deleteRouteByParcelId(@Valid @PathVariable UUID id){
            routeService.deleteRouteByParcelId(id);
            return ResponseEntity.status(OK).body("Zarejestrowana paczka w systemie usunięta!");
    }
    @GetMapping("/all/user/{username}")
    public List<Route> findRoutesByUsername(@PathVariable String username){
        List<Route> byUsername = routeService.findAllRoutes();
        return  byUsername.stream()
                .filter(p -> p.getUser().getUsername().equals(username))
                .collect(Collectors.toList());
    }
}
