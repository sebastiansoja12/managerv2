package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.ReturnPageDto;
import com.warehouse.returning.application.port.primary.ReturnPort;
import com.warehouse.returning.application.port.primary.ReturnQueryPort;
import com.warehouse.returning.application.port.primary.command.ChangeReturnPackageStatusCommand;
import com.warehouse.returning.application.port.primary.command.CreateReturnPackageCommand;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ChangeReturnPackageStatusRequest;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.CreateReturnPackageRequest;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnPackageRequestMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/returns/packages")
public class ReturnPackageController {

    private final ReturnPort returnPort;
    private final ReturnQueryPort returnQueryPort;
    private final CurrentUserApiService currentUserApiService;

    private final ReturnPackageRequestMapper returnPackageRequestMapper;

    public ReturnPackageController(final ReturnPort returnPort,
                                   final ReturnPackageRequestMapper returnPackageRequestMapper,
                                   final CurrentUserApiService currentUserApiService,
                                   final ReturnQueryPort returnQueryPort) {
        this.returnQueryPort = returnQueryPort;
        this.returnPort = returnPort;
        this.currentUserApiService = currentUserApiService;
        this.returnPackageRequestMapper = returnPackageRequestMapper;
    }

    @PostMapping
    public ResponseEntity<List<CreatedReturn>> create(@RequestBody @Valid final CreateReturnPackageRequest request) {
        final CreateReturnPackageCommand command = this.returnPackageRequestMapper.map(request, currentUserApiService.getCurrentUserId());
        final List<CreatedReturn> createdReturns = this.returnPort.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReturns);
    }

    @PutMapping("/{returnId}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable final ReturnPackageId returnId,
            @RequestBody @Valid final ChangeReturnPackageStatusRequest request) {
        final ChangeReturnPackageStatusCommand command = this.returnPackageRequestMapper.map(returnId, request);
        this.returnPort.changeStatus(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{returnId}")
    public ResponseEntity<ReturnDetailsDto> get(@PathVariable final ReturnPackageId returnId) {
        return ResponseEntity.ok(returnQueryPort.getDetails(returnId));
    }

    @PutMapping("/{returnId}/process")
    public ResponseEntity<Void> startProcessing(@PathVariable final ReturnPackageId returnId) {
        returnPort.startProcessing(returnId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{returnId}/complete")
    public ResponseEntity<Void> complete(@PathVariable final ReturnPackageId returnId) {
        returnPort.complete(returnId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{returnId}")
    public ResponseEntity<Void> cancel(@PathVariable final ReturnPackageId returnId) {
        returnPort.cancel(returnId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reason-code")
    public ResponseEntity<Void> changeReasonCode(@RequestBody @Valid final ChangeReasonCodeRequest request) {
        returnPort.changeReasonCode(request.returnPackageId(), request.reasonCode());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/token/validate")
    public TokenValidationResponse validateToken(@RequestBody @Valid final TokenValidationRequest request) {
        return new TokenValidationResponse(returnPort.validateToken(request.shipmentId(), request.returnToken()));
    }

    public record ChangeReasonCodeRequest(@NotNull ReturnPackageId returnPackageId, @NotNull ReasonCode reasonCode) {
    }

    public record TokenValidationRequest(@NotNull ShipmentId shipmentId, @NotNull ReturnToken returnToken) {
    }

    public record TokenValidationResponse(boolean valid) {
    }

    @GetMapping
    public ResponseEntity<ReturnPageDto> getReturns(
            @RequestParam final DepartmentId departmentId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "50") final int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Page must be non-negative and size must be between 1 and 100");
        }
        return ResponseEntity.ok(returnQueryPort.getReturns(departmentId, page, size));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidRequest(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
