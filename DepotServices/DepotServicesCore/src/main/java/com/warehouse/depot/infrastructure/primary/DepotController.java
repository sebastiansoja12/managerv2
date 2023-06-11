package com.warehouse.depot.infrastructure.primary;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotCodeDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.api.dto.DepotIdDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depots")
@AllArgsConstructor
public class DepotController {

    private final DepotService depotService;

    @PostMapping("/save")
    public ResponseEntity<?> add(@RequestBody DepotDto depot) {
        depotService.add(depot);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/save/multiple")
    public ResponseEntity<?> add(@RequestBody List<DepotDto> depots) {
        depotService.addMultipleDepots(depots);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/depotId/{value}")
    public ResponseEntity<?> viewDepotById(DepotIdDto depotId) {
        final DepotDto depot = depotService.viewDepotById(depotId);
        return ResponseEntity.status(HttpStatus.FOUND).body(depot);
    }

    @GetMapping("/depotCode/{value}")
    public ResponseEntity<?> viewDepotByCode(DepotCodeDto depotCode) {
        final DepotDto depot = depotService.viewDepotByCode(depotCode);
        return ResponseEntity.status(HttpStatus.FOUND).body(depot);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allDepots() {
        return ResponseEntity.status(HttpStatus.FOUND).body(depotService.findAll());
    }
}
