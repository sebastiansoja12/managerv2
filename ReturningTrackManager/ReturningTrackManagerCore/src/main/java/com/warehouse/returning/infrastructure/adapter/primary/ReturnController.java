package com.warehouse.returning.infrastructure.adapter.primary;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.ChangeReasonCodeRequest;
import com.warehouse.returning.domain.vo.DecodedApiTenant;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.DeleteReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ResponseStatus;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnResponseApi;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.returning.infrastructure.adapter.primary.validator.RequestValidator;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.BusinessException;

import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/returns")
@Slf4j
public class ReturnController {

    private final ReturnPort returnPort;

    private final RequestValidator requestValidator;

    private final ApiKeyService apiKeyService;

    public ReturnController(final ReturnPort returnPort,
                            final RequestValidator requestValidator,
                            final ApiKeyService apiKeyService) {
        this.returnPort = returnPort;
        this.requestValidator = requestValidator;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<?> process(
            @RequestHeader(value = "Authorization") final String authorizationHeader,
            @RequestBody @Valid final ReturnRequestApi returnApiRequest) {

        log.info("Received return request: {}", returnApiRequest.toString());

        final Result<Void, List<String>> validationResult = this.requestValidator.validateBody(returnApiRequest);

        if (validationResult.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getFailure());
        }

        ResponseEntity<?> responseEntity;

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            } else {
                final String token = authorizationHeader.substring(7);
                final DecodedApiTenant decodedApiTenant = this.apiKeyService.decodeJwt(token);

                log.info("Processing return request from user: {}", decodedApiTenant.userId().value());

                final ReturnRequest request = RequestMapper.map(returnApiRequest, decodedApiTenant);

                if (request.getRequests().isEmpty()) {
                    log.warn("Invalid return request: no items");
                    responseEntity = ResponseEntity.badRequest()
                            .body(Map.of("error", "Invalid request: no items to return"));
                } else {
                    final ReturnResponse response = this.returnPort.process(request);
                    final ReturnResponseApi responseApi = ResponseMapper.toResponseApi(response);

                    log.info("Return request processed successfully for user: {}", decodedApiTenant.userId().value());
                    responseEntity = ResponseEntity.ok(responseApi);
                }
            }
        } catch (final SignatureException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        } catch (final BusinessException e) {
            log.error("Business error while processing return request: {}", e.getMessage());
            responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("error", e.getMessage()));
        } catch (final Exception e) {
            log.error("Unexpected error during return processing", e);
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }

        return responseEntity;
    }


    @PutMapping("/reason-code")
    public ResponseEntity<?> updateReasonCode(@RequestBody final ChangeReasonCodeRequestApi changeReasonCodeRequest) {
        final ChangeReasonCodeRequest request = RequestMapper.map(changeReasonCodeRequest);
        this.returnPort.changeReasonCode(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{returnPackageId}")
    public ResponseEntity<?> get(@PathVariable final Long returnPackageId) {
        final ReturnPackageId returnId = new ReturnPackageId(returnPackageId);
        final ReturnPackage returnModel = returnPort.getReturn(returnId);
        return new ResponseEntity<>(ResponseMapper.toResponseApi(returnModel), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public DeleteReturnResponse delete(@PathVariable Long id) {
        final ReturnPackageId returnPackageId = new ReturnPackageId(id);
        returnPort.delete(returnPackageId);
        return new DeleteReturnResponse(ResponseStatus.OK);
    }


    @ExceptionHandler(RestException.class)
    public ResponseEntity<String> handleRestException(final RestException ex) {
        return ResponseEntity
                .status(ex.getCode())
                .body(ex.getMessage());
    }

}
