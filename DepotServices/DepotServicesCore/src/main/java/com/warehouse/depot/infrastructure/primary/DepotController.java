package com.warehouse.depot.infrastructure.primary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.primary.DepotPort;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depots")
@AllArgsConstructor
public class DepotController {

    private final DepotPort depotPort;

    @PostMapping("/save")
    public ResponseEntity<?> add(@RequestBody Depot depot) {
        depotPort.add(depot);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/save/multiple")
    public ResponseEntity<?> add(@RequestBody List<Depot> depots) {
        depotPort.addMultipleDepots(depots);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/depotId/{value}")
    public ResponseEntity<?> viewDepotById(DepotId depotId) {
        final Depot depot = depotPort.viewDepotById(depotId);
        return ResponseEntity.status(HttpStatus.FOUND).body(depot);
    }

    @GetMapping("/depotCode/{value}")
    public ResponseEntity<?> viewDepotByCode(DepotCode depotCode) {
        final Depot depot = depotPort.viewDepotByCode(depotCode);
        return ResponseEntity.status(HttpStatus.FOUND).body(depot);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allDepots() {
        return ResponseEntity.status(HttpStatus.FOUND).body(depotPort.findAll());
    }
}
