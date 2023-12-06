package com.warehouse.routetracker.infrastructure.adapter.primary;

import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.routetracker.infrastructure.api.dto.ParcelIdDto;
import com.warehouse.routetracker.infrastructure.api.dto.ZebraIdInformationDto;
import com.warehouse.routetracker.infrastructure.api.dto.ZebraVersionInformationDto;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routes/test")
@AllArgsConstructor
public class RouteTrackerTestController {

    private final RouteTrackerLogPort trackerLogPort;

    private final RouteRequestMapper requestMapper = Mappers.getMapper(RouteRequestMapper.class);

    private final RouteResponseMapper responseMapper = Mappers.getMapper(RouteResponseMapper.class);

    // TODO create DTOs

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
    public ResponseEntity<?> saveError(@RequestBody ErrorInformation errorInformation) {
        trackerLogPort.saveReturnErrorCode(errorInformation.getParcelId(), errorInformation.getError());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/terminalrequest")
    public ResponseEntity<?> saveTerminalRequest(@RequestBody TerminalRequest terminalRequest) {
        trackerLogPort.saveTerminalRequest(terminalRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/returntrackrequest")
    public ResponseEntity<?> saveReturnTrackRequest(@RequestBody ReturnTrackRequest request) {
        trackerLogPort.saveReturnTrackRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> getByParcelId(@PathVariable Long parcelId) {
        final RouteLogRecordToChange routeLogRecordToChange = trackerLogPort.find(parcelId);
        return new ResponseEntity<>(routeLogRecordToChange, HttpStatus.FOUND);
    }
}
