package com.warehouse.pickuppoint.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.exception.PickupPointCodeExistsException;
import com.warehouse.pickuppoint.application.exception.PickupPointDepartmentNotFoundException;
import com.warehouse.pickuppoint.application.exception.PickupPointNotFoundException;
import com.warehouse.pickuppoint.application.port.primary.PickupPointPort;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointPageResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointStatusTransitionException;
import com.warehouse.pickuppoint.domain.exception.PickupPointUnavailableException;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.ChangePickupPointStatusApiRequest;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.CreatePickupPointApiRequest;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointApiResponse;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointErrorApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointPageApiResponse;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.UpdatePickupPointApiRequest;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.mapper.PickupPointWebMapper;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pickup-points")
@AccessUserControl
public class PickupPointController {

    private final PickupPointPort pickupPointPort;
    private final PickupPointWebMapper pickupPointWebMapper;

    public PickupPointController(
            final PickupPointPort pickupPointPort,
            final PickupPointWebMapper pickupPointWebMapper) {
        this.pickupPointPort = pickupPointPort;
        this.pickupPointWebMapper = pickupPointWebMapper;
    }

    @GetMapping
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<PickupPointPageApiResponse> search(
            @RequestParam(required = false) final String query,
            @RequestParam(required = false) final PickupPointType type,
            @RequestParam(required = false) final PickupPointStatus status,
            @RequestParam(required = false) final PickupPointCapability capability,
            @RequestParam(required = false) final Long departmentId,
            @RequestParam(required = false) final CountryCode countryCode,
            @RequestParam(required = false) final String city,
            @RequestParam(required = false) final String networkCode,
            @RequestParam(required = false) final String bbox,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        final SearchPickupPointsCommand command = this.pickupPointWebMapper.toSearchCommand(
                query,
                type,
                status,
                capability,
                departmentId,
                countryCode,
                city,
                networkCode,
                bounds(bbox),
                null,
                null,
                page,
                size);
        return ResponseEntity.ok(this.pickupPointWebMapper.toResponse(this.pickupPointPort.search(command)));
    }

    @GetMapping("/eligible")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<PickupPointPageApiResponse> findEligible(
            @RequestParam final PickupPointCapability capability,
            @RequestParam final PickupPointType type,
            @RequestParam final CountryCode countryCode,
            @RequestParam final PickupPointShipmentSize shipmentSize,
            @RequestParam final boolean hasDangerousGoods,
            @RequestParam(required = false) final String query,
            @RequestParam(required = false) final String city,
            @RequestParam(required = false) final String bbox,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        final SearchPickupPointsCommand command = this.pickupPointWebMapper.toSearchCommand(
                query,
                type,
                null,
                capability,
                null,
                countryCode,
                city,
                null,
                bounds(bbox),
                shipmentSize,
                hasDangerousGoods,
                page,
                size);
        final PickupPointPageResult result = this.pickupPointPort.search(command);
        return ResponseEntity.ok(this.pickupPointWebMapper.toResponse(result));
    }

    @GetMapping("/{pickupPointId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<PickupPointApiResponse> get(@PathVariable final UUID pickupPointId) {
        final PickupPointResult result = this.pickupPointPort.get(new PickupPointId(pickupPointId))
                .orElseThrow(() -> new PickupPointNotFoundException(new PickupPointId(pickupPointId)));
        return ResponseEntity.ok(this.pickupPointWebMapper.toResponse(result));
    }

    @PostMapping
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
    public ResponseEntity<PickupPointApiResponse> create(
            @Valid @RequestBody final CreatePickupPointApiRequest request) {
        final PickupPointResult result = this.pickupPointPort.create(this.pickupPointWebMapper.toCommand(request));
        final PickupPointApiResponse response = this.pickupPointWebMapper.toResponse(result);
        return ResponseEntity.created(URI.create("/pickup-points/" + response.pickupPointId().value()))
                .body(response);
    }

    @PutMapping("/{pickupPointId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<PickupPointApiResponse> update(
            @PathVariable final UUID pickupPointId,
            @Valid @RequestBody final UpdatePickupPointApiRequest request) {
        final PickupPointResult result = this.pickupPointPort.update(
                this.pickupPointWebMapper.toCommand(new PickupPointId(pickupPointId), request));
        return ResponseEntity.ok(this.pickupPointWebMapper.toResponse(result));
    }

    @PutMapping("/{pickupPointId}/status")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<PickupPointApiResponse> changeStatus(
            @PathVariable final UUID pickupPointId,
            @Valid @RequestBody final ChangePickupPointStatusApiRequest request) {
        final PickupPointResult result = this.pickupPointPort.changeStatus(
                this.pickupPointWebMapper.toCommand(new PickupPointId(pickupPointId), request));
        return ResponseEntity.ok(this.pickupPointWebMapper.toResponse(result));
    }

    @ExceptionHandler(PickupPointNotFoundException.class)
    public ResponseEntity<PickupPointErrorApi> handleNotFound(final PickupPointNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, "PICKUP_POINT_NOT_FOUND", exception);
    }

    @ExceptionHandler({
            PickupPointCodeExistsException.class,
            OptimisticLockException.class
    })
    public ResponseEntity<PickupPointErrorApi> handleConflict(final RuntimeException exception) {
        return error(HttpStatus.CONFLICT, "PICKUP_POINT_CONFLICT", exception);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            InvalidPickupPointConfigurationException.class,
            InvalidPickupPointStatusTransitionException.class,
            PickupPointDepartmentNotFoundException.class,
            PickupPointUnavailableException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<PickupPointErrorApi> handleInvalidRequest(final Exception exception) {
        return error(HttpStatus.BAD_REQUEST, "PICKUP_POINT_REQUEST_INVALID", exception);
    }

    private double[] bounds(final String bbox) {
        if (bbox == null || bbox.isBlank()) {
            return null;
        }
        final String[] values = bbox.split(",");
        if (values.length != 4) {
            throw new IllegalArgumentException("Pickup point bounding box must contain west,south,east,north");
        }
        try {
            return new double[]{
                    Double.parseDouble(values[0].trim()),
                    Double.parseDouble(values[1].trim()),
                    Double.parseDouble(values[2].trim()),
                    Double.parseDouble(values[3].trim())
            };
        } catch (final NumberFormatException exception) {
            throw new IllegalArgumentException("Pickup point bounding box coordinates must be numbers", exception);
        }
    }

    private ResponseEntity<PickupPointErrorApi> error(
            final HttpStatus status,
            final String code,
            final Exception exception) {
        return ResponseEntity.status(status).body(new PickupPointErrorApi(code, exception.getMessage()));
    }
}
