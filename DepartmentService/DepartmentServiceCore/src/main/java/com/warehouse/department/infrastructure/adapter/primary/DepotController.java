package com.warehouse.department.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import com.warehouse.department.domain.model.Department;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.department.domain.port.primary.DepotPort;
import com.warehouse.department.domain.vo.DepotCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepotCodeDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepotDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateStreetRequestDto;
import com.warehouse.department.infrastructure.adapter.primary.mapper.DepotRequestMapper;
import com.warehouse.department.infrastructure.adapter.primary.mapper.DepotResponseMapper;

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
        final List<Department> departments = requestMapper.map(depotList);
        depotPort.addMultipleDepots(departments);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateStreet(@RequestBody UpdateStreetRequestDto updateStreetRequest) {
        final UpdateStreetRequest request = requestMapper.map(updateStreetRequest);
        depotPort.updateStreet(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/depotCode/{value}")
    public ResponseEntity<?> viewDepotByCode(DepotCodeDto code) {
        final DepotCode depotCode = requestMapper.map(code);
        final Department department = depotPort.viewDepotByCode(depotCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMapper.map(department));
    }

    @GetMapping
    public ResponseEntity<?> allDepots() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMapper.map(depotPort.findAll()));
    }
}
