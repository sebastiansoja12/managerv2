package com.warehouse.department.infrastructure.adapter.primary;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.warehouse.department.domain.exception.RestException;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.domain.vo.UpdateAddressRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateAddressApiRequest;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody final DepartmentCreateApiRequest departmentCreateApiRequest) {
//		final Result result = this.getValidator(departmentCreateApiRequest.getResourceName())
//				.validateBody(departmentCreateApiRequest);
//
//        if (result.isFailure()) {
//            return ResponseEntity.badRequest().body(result.getFailure());
//        }

        final DepartmentCreateRequest request = RequestMapper.map(departmentCreateApiRequest);

        try {
            final DepartmentCreateResponse response = this.departmentPort.createDepartments(request);
            return ResponseEntity.ok().body(ResponseMapper.mapToCreateApiResponse(response));
        } catch (final RestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody final UpdateAddressApiRequest updateAddressApiRequest) {
        final UpdateAddressRequest request = RequestMapper.map(updateAddressApiRequest);
        this.departmentPort.changeAddress(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{value}")
    public ResponseEntity<?> viewByDepartmentCode(@PathVariable final String value) {
        final DepartmentCode departmentCode = new DepartmentCode(value);
        final Department department = departmentPort.findByDepartmentCode(departmentCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseMapper.map(department));
    }

    @GetMapping
    public ResponseEntity<?> allDepartments() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.departmentPort.findAll().stream().map(ResponseMapper::map).toList());
    }
    
    private RequestValidator getValidator(final String resourceName) {
        return requestValidators.stream()
                .filter(validator -> validator.getResourceName().equals(resourceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Validator for resource " + resourceName + " not found"));
    }
}
