package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.returning.application.port.primary.ReturnReadModelQueryPort;
import com.warehouse.returning.configuration.JwtContext;
import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.DecodedApiOperator;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnPageApi;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ResponseMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/returns/read-model")
public class ReturnReadModelController {

    private final ReturnReadModelQueryPort returnReadModelQueryPort;

    private final ApiKeyService apiKeyService;

    public ReturnReadModelController(final ReturnReadModelQueryPort returnReadModelQueryPort,
                                     final ApiKeyService apiKeyService) {
        this.returnReadModelQueryPort = returnReadModelQueryPort;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping
    public ResponseEntity<ReturnPageApi> getAll(
            @RequestParam @NotNull(message = "Department ID is required") final DepartmentId departmentId,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be non-negative") final int page,
            @RequestParam(defaultValue = "50")
            @Min(value = 1, message = "Size must be between 1 and 100")
            @Max(value = 100, message = "Size must be between 1 and 100") final int size) {
        final DecodedApiOperator decodedApiOperator = this.apiKeyService.decodeJwt(JwtContext.getToken());
        if (decodedApiOperator.operatorId() == null) {
            throw new IllegalArgumentException("Operator ID is required");
        }
        return ResponseEntity.ok(ResponseMapper.toResponseApi(
                returnReadModelQueryPort.getReturns(departmentId, decodedApiOperator.operatorId(), page, size)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
