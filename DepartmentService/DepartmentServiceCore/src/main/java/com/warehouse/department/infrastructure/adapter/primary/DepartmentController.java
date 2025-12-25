package com.warehouse.department.infrastructure.adapter.primary;

import static org.springframework.http.HttpStatus.NON_AUTHORITATIVE_INFORMATION;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.CoordinatesDto;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.exception.RestException;
import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateCommand;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.vo.*;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.*;
import com.warehouse.department.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.department.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.department.infrastructure.adapter.primary.validator.DepartmentRequestValidator;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController implements DepartmentApiService {

    private final DepartmentPort departmentPort;

    private final Set<DepartmentRequestValidator> departmentRequestValidators;

    public DepartmentController(final DepartmentPort departmentPort,
                                final Set<DepartmentRequestValidator> departmentRequestValidators) {
        this.departmentPort = departmentPort;
        this.departmentRequestValidators = departmentRequestValidators;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> create(@RequestBody final DepartmentCreateApiRequest departmentCreateApiRequest) {
		final Result<Void, List<String>> result = this.getValidator(departmentCreateApiRequest.getResourceName()).validateBody(departmentCreateApiRequest);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }

        final DepartmentCreateCommand command = RequestMapper.map(departmentCreateApiRequest);

        try {
            final DepartmentCreateResponse response = this.departmentPort.createDepartments(command);
            return ResponseEntity.ok().body(ResponseMapper.mapToCreateApiResponse(response));
        } catch (final RestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> updateAddress(@RequestBody final UpdateAddressApiRequest updateAddressApiRequest) {
        final Result<Void, List<String>> result = this.getValidator(updateAddressApiRequest.getResourceName()).validateBody(updateAddressApiRequest);
        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }

        final UpdateAddressCommand request = RequestMapper.map(updateAddressApiRequest);
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
        final Result<Void, List<String>> result = this.getValidator(identificationNumberChangeRequest.getResourceName())
                .validateBody(identificationNumberChangeRequest);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }

        final IdentificationNumberChangeCommand request = RequestMapper.map(identificationNumberChangeRequest);
        final IdentificationNumberChangeResponse response = this.departmentPort.changeIdentificationNumber(request);
        return ResponseEntity.ok(ResponseMapper.map(response));
    }

    @PutMapping("/statuses")
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> changeDepartmentStatus(
            @RequestBody final ChangeDepartmentStatusApi departmentStatusRequest) {
        final Result<Void, List<String>> result = this.getValidator(departmentStatusRequest.getResourceName())
                .validateBody(departmentStatusRequest);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }
		final ChangeDepartmentStatusCommand command = new ChangeDepartmentStatusCommand(
				new DepartmentCode(departmentStatusRequest.departmentCode().value()), departmentStatusRequest.status());
        this.departmentPort.changeStatus(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/emails")
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> changeDepartmentEmail(
            @RequestBody final ChangeDepartmentEmailApiRequest changeDepartmentEmailRequest) {
        final Result<Void, List<String>> result = this.getValidator(changeDepartmentEmailRequest.getResourceName())
                .validateBody(changeDepartmentEmailRequest);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }
        final DepartmentCode departmentCode = new DepartmentCode(changeDepartmentEmailRequest.departmentCode().value());
        this.departmentPort.changeEmail(departmentCode, changeDepartmentEmailRequest.email());
        return ResponseEntity.ok().build();
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

    @GetMapping("/coordinates")
    @Override
    public List<DepartmentDto> getAllDepartments() {
        return this.departmentPort.findAll().stream().map(dep -> new DepartmentDto(dep.getDepartmentCode().getValue(),
                dep.getCity(), dep.getStreet(), dep.getCountryCode().name(),
                dep.getPostalCode(), new CoordinatesDto(dep.getCoordinates().lat(), dep.getCoordinates().lon()))).toList();
    }

    public DepartmentRequestValidator getValidator(final String resourceName) {
        return this.departmentRequestValidators.stream()
                .filter(validator -> validator.getResourceName().equals(resourceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No validator found for resource: " + resourceName));
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
