package com.warehouse.routetracker.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.vo.ParcelId;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.deliveryreturn.DeliveryReturnRequestDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.routetracker.configuration.common.RestException;

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

    @PostMapping("/initialize")
    public ResponseEntity<?> initialize(@RequestBody ShipmentIdDto id) {
        final ParcelId parcelId = requestMapper.map(id);
        final RouteProcess routeProcess = trackerLogPort.initializeRouteProcess(parcelId);
        return new ResponseEntity<>(responseMapper.map(routeProcess), HttpStatus.OK);

    }

    @PostMapping("/zebraidinformation")
    public ResponseEntity<?> saveZebraId(@RequestBody ZebraIdInformationDto information) {
        final ZebraIdInformation zebraIdInformation = requestMapper.map(information);
        trackerLogPort.saveZebraIdInformation(zebraIdInformation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/zebraversion")
    public ResponseEntity<?> saveZebraVersion(@RequestBody ZebraVersionInformationDto versionInformation) {
        final ZebraVersionInformation zebraVersionInformation = requestMapper.map(versionInformation);
        trackerLogPort.saveZebraVersionInformation(zebraVersionInformation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/error")
    public ResponseEntity<?> saveError(@RequestBody ErrorInformationDto errorInformation) {
        final ErrorInformation information = requestMapper.map(errorInformation);
        trackerLogPort.saveReturnErrorCode(information);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/suppliercode")
    public ResponseEntity<?> saveSupplierCode(@RequestBody SupplierCodeRequestDto supplierCodeRequest) {
        final SupplierCodeRequest request = requestMapper.map(supplierCodeRequest);
        trackerLogPort.saveSupplierCode(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/depotcode")
    public ResponseEntity<?> saveDepotCode(@RequestBody DepotCodeRequestDto depotCodeRequest) {
        final DepotCodeRequest request = requestMapper.map(depotCodeRequest);
        trackerLogPort.saveDepotCode(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/username")
    public ResponseEntity<?> saveUsername(@RequestBody UsernameRequestDto usernameRequest) {
        final UsernameRequest request = requestMapper.map(usernameRequest);
        trackerLogPort.saveUsername(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/description")
    public ResponseEntity<?> saveDescription(@RequestBody DescriptionRequestDto descriptionRequest) {
        final DescriptionRequest request = requestMapper.map(descriptionRequest);
        trackerLogPort.saveDescription(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/zebra-initialize-request")
    public ResponseEntity<?> saveZebraInitializeRequest(@RequestBody ZebraInitializeRequestDto initializeRequest) {
        final ZebraInitializeRequest request = requestMapper.map(initializeRequest);
        trackerLogPort.saveCreateRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/terminalrequest")
    public ResponseEntity<?> saveTerminalRequest(@RequestBody TerminalRequestDto terminalRequest) {
        final TerminalRequest request = requestMapper.map(terminalRequest);
        trackerLogPort.saveTerminalRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/returntrackrequest")
    public ResponseEntity<?> saveReturnTrackRequest(@RequestBody ReturnTrackRequestDto returnTrackRequest) {
        final ReturnTrackRequest request = requestMapper.map(returnTrackRequest);
        trackerLogPort.saveReturnTrackRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delivery-return-request")
    public ResponseEntity<?> saveDeliveryReturnRequest(@RequestBody DeliveryReturnRequestDto deliveryReturnRequest) {
        final DeliveryReturnRequest request = requestMapper.map(deliveryReturnRequest);
        trackerLogPort.saveDeliveryReturnRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delivery")
    public ResponseEntity<?> saveDeliveryStatus(@RequestBody DeliveryStatusRequestDto deliveryStatusRequest) {
        final DeliveryStatusRequest request = requestMapper.map(deliveryStatusRequest);
        trackerLogPort.saveDeliveryStatus(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> getByParcelId(@PathVariable Long parcelId) {
        final RouteLogRecord routeLogRecord = trackerLogPort.find(parcelId);
        return new ResponseEntity<>(responseMapper.map(routeLogRecord), HttpStatus.OK);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(RestException ex) {
        final ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(error.getStatus()));
    }
}
