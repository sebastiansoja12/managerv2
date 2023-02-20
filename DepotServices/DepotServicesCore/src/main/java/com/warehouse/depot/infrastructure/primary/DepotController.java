package com.warehouse.depot.infrastructure.primary;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotCodeDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.api.dto.DepotIdDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depots")
@AllArgsConstructor
public class DepotController {

    private final DepotService depotService;

    @PostMapping("/save")
    public void add(@RequestBody DepotDto depot) {
        depotService.add(depot);
    }

    @PostMapping("/save/multiple")
    public void add(@RequestBody List<DepotDto> depots) {
        depotService.addMultipleDepots(depots);
    }

    @GetMapping("/depotId/{value}")
    public DepotDto viewDepotById(DepotIdDto depotId) {
        return depotService.viewDepotById(depotId);
    }

    @GetMapping("/depotCode/{value}")
    public DepotDto viewDepotByCode(DepotCodeDto depotCode) {
        return depotService.viewDepotByCode(depotCode);
    }

    @GetMapping("/all")
    public List<DepotDto> allDepots() {
        return depotService.findAll();
    }


}
