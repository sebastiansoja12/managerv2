package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryHistoryPort;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryNotFoundException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryStepNotFoundException;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryAttemptRegisterRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryConditionsRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryHistoryEntryResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryRelationResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryRouteRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepCarrierRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepCommentRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepSignatureRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepStatusRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliverySupplierRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryPort deliveryPort;
    private final DeliveryHistoryPort deliveryHistoryPort;
    private final DeliveryResponseMapper responseMapper;
    private final DeliveryRequestMapper requestMapper;

    public DeliveryController(final DeliveryPort deliveryPort,
                              final DeliveryHistoryPort deliveryHistoryPort,
                              final DeliveryResponseMapper responseMapper,
                              final DeliveryRequestMapper requestMapper) {
        this.deliveryPort = deliveryPort;
        this.deliveryHistoryPort = deliveryHistoryPort;
        this.responseMapper = responseMapper;
        this.requestMapper = requestMapper;
    }

    @GetMapping
    public List<DeliveryResponseDto> findRecent(@RequestParam(defaultValue = "0") final int offset,
                                                @RequestParam(defaultValue = "25") final int limit,
                                                @RequestParam(required = false) final Long departmentId,
                                                @RequestParam(required = false) final Long supplierId) {
        if (offset < 0 || limit < 1 || limit > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delivery page");
        }
        if (supplierId != null) {
            return responseMapper.map(deliveryPort.findDeliveriesBySupplier(new SupplierId(supplierId), offset, limit));
        }
        if (departmentId != null) {
            return responseMapper.map(deliveryPort.findDeliveriesByDepartment(new DepartmentId(departmentId),
                    offset, limit));
        }
        return responseMapper.map(deliveryPort.findRecentDeliveries(offset, limit));
    }

    @GetMapping("/{deliveryId}")
    public DeliveryResponseDto findById(@PathVariable final String deliveryId) {
        return responseMapper.map(deliveryPort.findDelivery(new DeliveryId(deliveryId)));
    }

    @GetMapping("/{deliveryId}/related")
    public List<DeliveryRelationResponseDto> findRelated(@PathVariable final String deliveryId) {
        return responseMapper.mapRelations(deliveryPort.findRelatedDeliveries(new DeliveryId(deliveryId)));
    }

    @GetMapping("/{deliveryId}/history")
    public List<DeliveryHistoryEntryResponseDto> findHistory(@PathVariable final String deliveryId) {
        return responseMapper.mapHistory(deliveryHistoryPort.findHistory(new DeliveryId(deliveryId)));
    }

    @GetMapping("/by-shipment/{shipmentId}")
    public DeliveryResponseDto findByShipmentId(@PathVariable final Long shipmentId) {
        return responseMapper.map(deliveryPort.findDeliveryByShipmentId(new ShipmentId(shipmentId)));
    }

    @GetMapping("/by-pallet/{palletId}")
    public DeliveryResponseDto findByPalletId(@PathVariable final String palletId) {
        return responseMapper.map(deliveryPort.findDeliveryByPalletId(palletId));
    }

    @PutMapping("/{deliveryId}/supplier")
    public DeliveryResponseDto assignSupplier(@PathVariable final String deliveryId,
                                              @RequestBody final DeliverySupplierRequestDto request) {
        if (request == null || request.supplierId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier is required");
        }
        return responseMapper.map(deliveryPort.assignSupplier(requestMapper.map(new DeliveryId(deliveryId), request)));
    }

    @DeleteMapping("/{deliveryId}/supplier")
    public DeliveryResponseDto unassignSupplier(@PathVariable final String deliveryId) {
        return responseMapper.map(deliveryPort.unassignSupplier(new DeliveryId(deliveryId)));
    }

    @PutMapping("/{deliveryId}/route")
    public DeliveryResponseDto changeRoute(@PathVariable final String deliveryId,
                                           @RequestBody final DeliveryRouteRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route is required");
        }
        return responseMapper.map(deliveryPort.changeRoute(requestMapper.map(new DeliveryId(deliveryId), request)));
    }

    @PutMapping("/{deliveryId}/conditions")
    public DeliveryResponseDto changeConditions(@PathVariable final String deliveryId,
                                                @RequestBody final DeliveryConditionsRequestDto request) {
        if (request == null || request.method() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery method is required");
        }
        return responseMapper.map(deliveryPort.changeConditions(requestMapper.map(new DeliveryId(deliveryId), request)));
    }

    @PostMapping("/{deliveryId}/attempts")
    public DeliveryResponseDto registerAttempt(@PathVariable final String deliveryId,
                                               @RequestBody final DeliveryAttemptRegisterRequestDto request) {
        if (request == null || request.deliveryStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery status is required");
        }
        return responseMapper.map(deliveryPort.registerAttempt(requestMapper.map(new DeliveryId(deliveryId), request)));
    }

    @PostMapping("/{deliveryId}/complete")
    public DeliveryResponseDto complete(@PathVariable final String deliveryId) {
        return responseMapper.map(deliveryPort.finishDelivery(new DeliveryId(deliveryId)));
    }

    @PutMapping("/{deliveryId}/steps/{stepNumber}/comment")
    public DeliveryResponseDto commentStep(@PathVariable final String deliveryId,
                                           @PathVariable final int stepNumber,
                                           @RequestBody final DeliveryStepCommentRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment is required");
        }
        return responseMapper.map(deliveryPort.commentStep(
                requestMapper.map(new DeliveryId(deliveryId), stepNumber, request)));
    }

    @PutMapping("/{deliveryId}/steps/{stepNumber}/status")
    public DeliveryResponseDto correctStepStatus(@PathVariable final String deliveryId,
                                                 @PathVariable final int stepNumber,
                                                 @RequestBody final DeliveryStepStatusRequestDto request) {
        if (request == null || request.deliveryStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery status is required");
        }
        return responseMapper.map(deliveryPort.correctStepStatus(
                requestMapper.map(new DeliveryId(deliveryId), stepNumber, request)));
    }

    @PutMapping("/{deliveryId}/steps/{stepNumber}/signature")
    public DeliveryResponseDto attachStepSignature(@PathVariable final String deliveryId,
                                                   @PathVariable final int stepNumber,
                                                   @RequestBody final DeliveryStepSignatureRequestDto request) {
        if (request == null || request.signatureId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Signature is required");
        }
        return responseMapper.map(deliveryPort.attachStepSignature(
                requestMapper.map(new DeliveryId(deliveryId), stepNumber, request)));
    }

    @PutMapping("/{deliveryId}/steps/{stepNumber}/carrier")
    public DeliveryResponseDto reassignStepCarrier(@PathVariable final String deliveryId,
                                                   @PathVariable final int stepNumber,
                                                   @RequestBody final DeliveryStepCarrierRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carrier is required");
        }
        return responseMapper.map(deliveryPort.reassignStepCarrier(
                requestMapper.map(new DeliveryId(deliveryId), stepNumber, request)));
    }

    @PostMapping("/{deliveryId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable final String deliveryId) {
        final DeliveryId id = new DeliveryId(deliveryId);
        this.deliveryPort.cancelDelivery(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({DeliveryNotFoundException.class, DeliveryStepNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }

    @ExceptionHandler(DeliveryModificationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void conflict() {
    }
}
