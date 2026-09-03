package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.logistics.infrastructure.adapter.primary.dto.DeliveryMethodUpdateRequestDto;
import com.warehouse.logistics.infrastructure.adapter.primary.dto.DeliveryResponseDto;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/deliveries")
public class DeliveryReadController {

    private final LogisticsPort logisticsPort;
    private final DeliveryResponseMapper responseMapper;

    public DeliveryReadController(final LogisticsPort logisticsPort,
                                  final DeliveryResponseMapper responseMapper) {
        this.logisticsPort = logisticsPort;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public List<DeliveryResponseDto> findRecent(@RequestParam(defaultValue = "0") final int offset,
                                                @RequestParam(defaultValue = "25") final int limit) {
        if (offset < 0 || limit < 1 || limit > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delivery page");
        }
        return responseMapper.map(logisticsPort.findRecentDeliveries(offset, limit));
    }

    @GetMapping("/{deliveryId}")
    public DeliveryResponseDto findById(@PathVariable final String deliveryId) {
        return responseMapper.map(logisticsPort.findDelivery(new DeliveryId(deliveryId)));
    }

    @GetMapping("/by-shipment/{shipmentId}")
    public DeliveryResponseDto findByShipmentId(@PathVariable final Long shipmentId) {
        return responseMapper.map(logisticsPort.findDeliveryByShipmentId(new ShipmentId(shipmentId)));
    }

    @PutMapping("/{deliveryId}/method")
    public ResponseEntity<Void> changeMethod(@PathVariable final String deliveryId,
                                               @RequestBody final DeliveryMethodUpdateRequestDto request) {
        if (request == null || request.method() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery method is required");
        }
        this.logisticsPort.changeDeliveryMethod(new DeliveryId(deliveryId), request.method());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable final String deliveryId) {
        final DeliveryId id = new DeliveryId(deliveryId);
        this.logisticsPort.cancelDelivery(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void conflict() {
    }
}
