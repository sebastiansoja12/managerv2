package com.warehouse.routetracker.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.configuration.common.RestException;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.DeviceInformationRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentCreatedRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.deliveryreturn.DeliveryReturnRequestDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteTrackerController {

    private final RouteTrackerLogPort trackerLogPort;

    private final RouteRequestMapper requestMapper = Mappers.getMapper(RouteRequestMapper.class);

    private final RouteResponseMapper responseMapper = Mappers.getMapper(RouteResponseMapper.class);

    @GetMapping
    public ResponseEntity<?> getAll() {
        final List<RouteLogRecord> routeLogRecord = trackerLogPort.findAll();
        return new ResponseEntity<>(responseMapper.mapToLogRecord(routeLogRecord), HttpStatus.OK);
    }

    @PostMapping("/shipments")
    public ResponseEntity<?> initialize(@RequestBody final ShipmentCreatedRequest request) {
        final RouteProcess routeProcess = trackerLogPort.initializeRouteProcess(new ShipmentId(request.shipmentId().value()));
        return new ResponseEntity<>(responseMapper.map(routeProcess), HttpStatus.OK);

    }

    @PostMapping("/terminal-id")
    public ResponseEntity<?> saveTerminalId(@RequestBody final TerminalIdRequestDto request) {
        final DeviceIdInformation deviceIdInformation = requestMapper.map(request);
        trackerLogPort.saveDeviceIdInformation(deviceIdInformation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/terminal-version")
    public ResponseEntity<?> saveZebraVersion(@RequestBody final TerminalVersionRequestDto versionRequest) {
        final DeviceVersionInformation deviceVersionInformation = requestMapper.map(versionRequest);
        trackerLogPort.saveDeviceVersionInformation(deviceVersionInformation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/error")
    public ResponseEntity<?> saveError(@RequestBody final ErrorInformationRequestDto errorRequest) {
        final ErrorInformation information = requestMapper.map(errorRequest);
        trackerLogPort.saveReturnErrorCode(information);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/supplier-code")
    public ResponseEntity<?> saveSupplierCode(@RequestBody final SupplierCodeRequestDto supplierCodeRequest) {
        final SupplierCodeRequest request = requestMapper.map(supplierCodeRequest);
        trackerLogPort.saveSupplierCode(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/depot-code")
    public ResponseEntity<?> saveDepotCode(@RequestBody final DepotCodeRequestDto depotCodeRequest) {
        final DepotCodeRequest request = requestMapper.map(depotCodeRequest);
        trackerLogPort.saveDepotCode(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/username")
    public ResponseEntity<?> saveUsername(@RequestBody final UsernameRequestDto usernameRequest) {
        final UsernameRequest request = requestMapper.map(usernameRequest);
        trackerLogPort.saveUsername(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/description")
    public ResponseEntity<?> saveDescription(@RequestBody final DescriptionRequestDto descriptionRequest) {
        final DescriptionRequest request = requestMapper.map(descriptionRequest);
        trackerLogPort.saveDescription(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/terminal-request")
    public ResponseEntity<?> saveTerminalRequest(@RequestBody final TerminalRequestDto terminalRequest) {
        final TerminalRequest request = requestMapper.map(terminalRequest);
        trackerLogPort.saveTerminalRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/return-track-request")
    public ResponseEntity<?> saveReturnTrackRequest(@RequestBody final ReturnTrackRequestDto returnTrackRequest) {
        final ReturnTrackRequest request = requestMapper.map(returnTrackRequest);
        trackerLogPort.saveReturnTrackRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delivery-return-request")
    public ResponseEntity<?> saveDeliveryReturnRequest(@RequestBody final DeliveryReturnRequestDto deliveryReturnRequest) {
        final DeliveryReturnRequest request = requestMapper.map(deliveryReturnRequest);
        trackerLogPort.saveDeliveryReturnRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delivery")
    public ResponseEntity<?> saveDeliveryStatus(@RequestBody final DeliveryStatusRequestDto deliveryStatusRequest) {
        final DeliveryStatusRequest request = requestMapper.map(deliveryStatusRequest);
        trackerLogPort.saveDeliveryStatus(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/device-information")
    public ResponseEntity<?> saveDeviceInformation(@RequestBody final DeviceInformationRequestDto deviceInformationRequest) {
        final DeviceInformationRequest request = DeviceInformationRequest.from(deviceInformationRequest);
        trackerLogPort.saveDeviceInformation(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> getByParcelId(@PathVariable Long parcelId) {
        final ShipmentId shipmentId = new ShipmentId(parcelId);
        final RouteLogRecord routeLogRecord = trackerLogPort.find(shipmentId);
        return new ResponseEntity<>(responseMapper.map(routeLogRecord), HttpStatus.OK);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(RestException ex) {
        final ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(error.getStatus()));
    }
}
