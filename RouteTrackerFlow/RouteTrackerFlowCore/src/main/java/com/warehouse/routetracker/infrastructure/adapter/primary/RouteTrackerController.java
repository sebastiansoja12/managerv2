package com.warehouse.routetracker.infrastructure.adapter.primary;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.vo.ParcelId;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;
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

    @PostMapping("/initialize")
    public ResponseEntity<?> initialize(@RequestBody ParcelIdDto id) {
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

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> getByParcelId(@PathVariable Long parcelId) {
        final RouteLogRecord routeLogRecord = trackerLogPort.find(parcelId);
        return new ResponseEntity<>(responseMapper.map(routeLogRecord), HttpStatus.OK);
    }
}
