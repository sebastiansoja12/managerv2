package com.warehouse.depot.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import com.warehouse.depot.infrastructure.adapter.primary.api.dto.DepotCodeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.infrastructure.adapter.primary.api.dto.DepotDto;
import com.warehouse.depot.infrastructure.adapter.primary.mapper.DepotRequestMapper;
import com.warehouse.depot.infrastructure.adapter.primary.mapper.DepotResponseMapper;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/depots")
@AllArgsConstructor
public class DepotController {

    private final DepotPort depotPort;

    private final DepotRequestMapper requestMapper = getMapper(DepotRequestMapper.class);

    private final DepotResponseMapper responseMapper = getMapper(DepotResponseMapper.class);


    @PostMapping
    public ResponseEntity<?> add(@RequestBody List<DepotDto> depotList) {
        final List<Depot> depots = requestMapper.map(depotList);
        depotPort.addMultipleDepots(depots);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/depotCode/{value}")
    public ResponseEntity<?> viewDepotByCode(DepotCodeDto code) {
        final DepotCode depotCode = requestMapper.map(code);
        final Depot depot = depotPort.viewDepotByCode(depotCode);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(responseMapper.map(depot));
    }

    @GetMapping
    public ResponseEntity<?> allDepots() {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(responseMapper.map(depotPort.findAll()));
    }
}
