package com.warehouse.routetracker.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.routetracker.configuration.common.RestException;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.ErrorResponseDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteTrackerController {

    private final RouteTrackerLogPort trackerLogPort;

    private final RouteResponseMapper responseMapper = Mappers.getMapper(RouteResponseMapper.class);

    @GetMapping
    public ResponseEntity<?> getAll() {
        final List<RouteLogRecord> routeLogRecords = this.trackerLogPort.findAll();
        return new ResponseEntity<>(this.responseMapper.mapToLogRecord(routeLogRecords), HttpStatus.OK);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<?> getByShipmentId(@PathVariable final Long shipmentId) {
        final RouteLogRecord routeLogRecord = this.trackerLogPort.find(new ShipmentId(shipmentId));
        return new ResponseEntity<>(this.responseMapper.map(routeLogRecord), HttpStatus.OK);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(final RestException exception) {
        final ErrorResponseDto error =
                new ErrorResponseDto(LocalDateTime.now(), exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(error.getStatus()));
    }
}
