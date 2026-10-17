package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.OperatorDirectoryPort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreateOperatorCourierRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreateOperatorDepartmentRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreateOperatorUserRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorCourierResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDepartmentResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorUserResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ValueResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.OperatorDirectoryRequestMapper;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.OperatorDirectoryResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/operators/{operatorId}")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorDirectoryController {

    private final OperatorDirectoryPort operatorDirectoryPort;

    public OperatorDirectoryController(final OperatorDirectoryPort operatorDirectoryPort) {
        this.operatorDirectoryPort = operatorDirectoryPort;
    }

    @GetMapping("/users")
    public ResponseEntity<List<OperatorUserResponse>> findUsers(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok(operatorDirectoryPort.findUsers(OperatorId.of(operatorId))
                .stream()
                .map(OperatorDirectoryResponseMapper::toResponse)
                .toList());
    }

    @PostMapping("/users")
    public ResponseEntity<ValueResponse<Long>> createUser(@PathVariable @Positive final Long operatorId,
                                                          @Valid @RequestBody final CreateOperatorUserRequest request) {
        final UserId userId = operatorDirectoryPort.createUser(OperatorId.of(operatorId),
                OperatorDirectoryRequestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ValueResponse<>(userId.value()));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<OperatorDepartmentResponse>> findDepartments(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok(operatorDirectoryPort.findDepartments(OperatorId.of(operatorId))
                .stream()
                .map(OperatorDirectoryResponseMapper::toResponse)
                .toList());
    }

    @PostMapping("/departments")
    public ResponseEntity<ValueResponse<String>> createDepartment(
            @PathVariable @Positive final Long operatorId,
            @Valid @RequestBody final CreateOperatorDepartmentRequest request) {
        final DepartmentCode departmentCode = operatorDirectoryPort.createDepartment(OperatorId.of(operatorId),
                OperatorDirectoryRequestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ValueResponse<>(departmentCode.getValue()));
    }

    @GetMapping("/couriers")
    public ResponseEntity<List<OperatorCourierResponse>> findCouriers(@PathVariable @Positive final Long operatorId) {
        return ResponseEntity.ok(operatorDirectoryPort.findCouriers(OperatorId.of(operatorId))
                .stream()
                .map(OperatorDirectoryResponseMapper::toResponse)
                .toList());
    }

    @PostMapping("/couriers")
    public ResponseEntity<ValueResponse<String>> createCourier(
            @PathVariable @Positive final Long operatorId,
            @Valid @RequestBody final CreateOperatorCourierRequest request) {
        final SupplierCode supplierCode = operatorDirectoryPort.createCourier(OperatorId.of(operatorId),
                OperatorDirectoryRequestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ValueResponse<>(supplierCode.value()));
    }

    @ExceptionHandler(OperatorNotFoundException.class)
    public ResponseEntity<String> handleOperatorNotFound(final OperatorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
