package com.warehouse.reroute.infrastructure.adapter.primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.reroute.application.port.primary.RerouteTokenPort;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ReroutableShipmentApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteAcceptedApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ReroutableShipmentSummaryApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteShipmentApiRequest;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteTokenIssuedApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteTokenVerificationApiRequest;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteApiMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.validator.RerouteRequestValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reroutes/shipments")
public class RerouteController {

    private final RerouteTokenPort rerouteTokenPort;

    private final RerouteApiMapper rerouteApiMapper;

    private final RerouteRequestValidator rerouteRequestValidator;

    public RerouteController(final RerouteTokenPort rerouteTokenPort,
                             final RerouteApiMapper rerouteApiMapper,
                             final RerouteRequestValidator rerouteRequestValidator) {
        this.rerouteTokenPort = rerouteTokenPort;
        this.rerouteApiMapper = rerouteApiMapper;
        this.rerouteRequestValidator = rerouteRequestValidator;
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<ReroutableShipmentSummaryApiResponse> getShipment(@PathVariable final String trackingNumber) {
        return ResponseEntity.ok(rerouteApiMapper.map(
                rerouteTokenPort.findShipment(rerouteApiMapper.mapTrackingNumber(trackingNumber))));
    }

    @PostMapping("/{trackingNumber}/tokens")
    public ResponseEntity<RerouteTokenIssuedApiResponse> requestToken(@PathVariable final String trackingNumber) {
        return ResponseEntity.ok(rerouteApiMapper.map(
                rerouteTokenPort.requestToken(rerouteApiMapper.mapTrackingNumber(trackingNumber))));
    }

    @PostMapping("/{trackingNumber}/tokens/verifications")
    public ResponseEntity<ReroutableShipmentApiResponse> verifyToken(
            @PathVariable final String trackingNumber,
            @Valid @RequestBody final RerouteTokenVerificationApiRequest request) {
        return ResponseEntity.ok(rerouteApiMapper.map(
                rerouteTokenPort.verifyToken(rerouteApiMapper.map(trackingNumber, request))));
    }

    @PutMapping("/{trackingNumber}")
    public ResponseEntity<RerouteAcceptedApiResponse> reroute(
            @PathVariable final String trackingNumber,
            @Valid @RequestBody final RerouteShipmentApiRequest request) {
        rerouteRequestValidator.validate(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rerouteApiMapper.map(
                rerouteTokenPort.reroute(rerouteApiMapper.map(trackingNumber, request))));
    }
}
