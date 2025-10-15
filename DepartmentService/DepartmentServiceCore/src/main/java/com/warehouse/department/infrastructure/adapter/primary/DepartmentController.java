package com.warehouse.department.infrastructure.adapter.primary;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.department.domain.exception.RestException;
import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.domain.vo.UpdateStreetRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateStreetRequestDto;
import com.warehouse.department.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.department.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.department.infrastructure.adapter.primary.validator.RequestValidator;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentPort departmentPort;

    private final Set<RequestValidator> requestValidators;

    public DepartmentController(final DepartmentPort departmentPort,
                                final Set<RequestValidator> requestValidators) {
        this.departmentPort = departmentPort;
        this.requestValidators = requestValidators;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final DepartmentCreateApiRequest departmentCreateApiRequest) {
		final Result result = this.getValidator(departmentCreateApiRequest.getResourceName())
				.validateBody(departmentCreateApiRequest);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }

        final DepartmentCreateRequest request = RequestMapper.map(departmentCreateApiRequest);

        try {
            final DepartmentCreateResponse response = this.departmentPort.createDepartments(request);
            return ResponseEntity.ok().body(ResponseMapper.mapToCreateApiResponse(response));
        } catch (final RestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> add(@RequestBody List<DepartmentDto> depotList) {
        final List<Department> departments = null;
        departmentPort.addDepartments(departments);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateStreet(@RequestBody final UpdateStreetRequestDto updateStreetRequest) {
        final UpdateStreetRequest request = null;
        departmentPort.updateStreet(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/departmentCode/{value}")
    public ResponseEntity<?> viewByDepartmentCode(@PathVariable String value) {
        final DepartmentCode departmentCode = new DepartmentCode(value);
        final Department department = departmentPort.findByDepartmentCode(departmentCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(department);
    }

    @GetMapping
    public ResponseEntity<?> allDepartments() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }
    
    private RequestValidator getValidator(final String resourceName) {
        return requestValidators.stream()
                .filter(validator -> validator.getResourceName().equals(resourceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Validator for resource " + resourceName + " not found"));
    }
}
