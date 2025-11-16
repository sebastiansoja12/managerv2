package com.warehouse.department.infrastructure.adapter.primary;

import com.warehouse.department.domain.enumeration.DepartmentType;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NON_AUTHORITATIVE_INFORMATION;

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
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> updateAddress(@RequestBody final UpdateAddressApiRequest updateAddressApiRequest) {
        final UpdateAddressRequest request = RequestMapper.map(updateAddressApiRequest);
        this.departmentPort.changeAddress(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/active-departments")
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> updateDepartmentActiveStatus(@RequestParam final Boolean active,
                                                          @RequestParam final String departmentCode) {
        final DepartmentCode departmentCodeValue = new DepartmentCode(departmentCode);
        this.departmentPort.changeDepartmentActive(departmentCodeValue, active);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/department-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> changeDepartmentType(@RequestParam final String departmentType,
                                                  @RequestParam final String departmentCode) {
        final DepartmentCode departmentCodeValue = new DepartmentCode(departmentCode);
        final DepartmentType type = DepartmentType.valueOf(departmentType);
        this.departmentPort.changeDepartmentType(departmentCodeValue, type);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/identification-numbers")
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> updateIdentificationNumber(
			@RequestBody final IdentificationNumberChangeApiRequest identificationNumberChangeRequest) {
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

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleRestException(final RestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleRestException(final AuthorizationDeniedException e) {
        return ResponseEntity.status(NON_AUTHORITATIVE_INFORMATION).body(e.getMessage());
    }
}
