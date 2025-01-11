package com.warehouse.department.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCodeDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateStreetRequestDto;
import com.warehouse.department.infrastructure.adapter.primary.mapper.DepotRequestMapper;
import com.warehouse.department.infrastructure.adapter.primary.mapper.DepotResponseMapper;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentPort departmentPort;

    private final DepotRequestMapper requestMapper = getMapper(DepotRequestMapper.class);

    private final DepotResponseMapper responseMapper = getMapper(DepotResponseMapper.class);


    @PostMapping
    public ResponseEntity<?> add(@RequestBody List<DepartmentDto> depotList) {
        final List<Department> departments = requestMapper.map(depotList);
        departmentPort.addDepartments(departments);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateStreet(@RequestBody final UpdateStreetRequestDto updateStreetRequest) {
        final UpdateStreetRequest request = requestMapper.map(updateStreetRequest);
        departmentPort.updateStreet(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/departmentCode/{value}")
    public ResponseEntity<?> viewByDepartmentCode(DepartmentCodeDto code) {
        final DepartmentCode departmentCode = requestMapper.map(code);
        final Department department = departmentPort.findByDepartmentCode(departmentCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMapper.map(department));
    }

    @GetMapping
    public ResponseEntity<?> allDepartments() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMapper.map(departmentPort.findAll()));
    }
}
