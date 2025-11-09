package com.warehouse.department.infrastructure.adapter.primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.warehouse.department.domain.exception.RestException;
import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.vo.*;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.IdentificationNumberChangeApiRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateAddressApiRequest;
import com.warehouse.department.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.department.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.department.infrastructure.adapter.primary.validator.DepartmentCreateApiDepartmentRequestValidator;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

    private final DepartmentPort departmentPort;

    private final DepartmentCreateApiDepartmentRequestValidator departmentRequestValidator;

    public DepartmentController(final DepartmentPort departmentPort,
                                final DepartmentCreateApiDepartmentRequestValidator departmentRequestValidator) {
        this.departmentPort = departmentPort;
        this.departmentRequestValidator = departmentRequestValidator;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody final DepartmentCreateApiRequest departmentCreateApiRequest) {
		final Result result = this.departmentRequestValidator.validateBody(departmentCreateApiRequest);

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

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody final UpdateAddressApiRequest updateAddressApiRequest) {
        final UpdateAddressRequest request = RequestMapper.map(updateAddressApiRequest);
        this.departmentPort.changeAddress(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/identification-numbers")
    public ResponseEntity<?> updateNip(@RequestBody final IdentificationNumberChangeApiRequest identificationNumberChangeRequest) {
        final IdentificationNumberChangeRequest request = RequestMapper.map(identificationNumberChangeRequest);
        final IdentificationNumberChangeResponse response = this.departmentPort.changeIdentificationNumber(request);
        return ResponseEntity.ok(ResponseMapper.map(response));
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
}
