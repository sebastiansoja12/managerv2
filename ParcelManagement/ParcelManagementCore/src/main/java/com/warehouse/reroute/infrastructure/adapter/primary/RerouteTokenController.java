package com.warehouse.reroute.infrastructure.adapter.primary;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.primary.RerouteUpdatePort;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapper;
import com.warehouse.reroute.infrastructure.api.dto.RerouteParcelRequestDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reroutes")
@AllArgsConstructor
public class RerouteTokenController {

    private final RerouteTokenPort port;

    private final RerouteUpdatePort rerouteUpdatePort;

    private final RerouteTokenRequestMapper requestMapper;

    private final RerouteTokenResponseMapper responseMapper;

    @PostMapping("/information")
    public ResponseEntity<?> sendInformation(@RequestBody RerouteRequestDto requestDto) {
        final RerouteRequest request = requestMapper.map(requestDto);
        final RerouteResponse rerouteResponse = port.sendReroutingInformation(request);
        return new ResponseEntity<>(responseMapper.map(rerouteResponse), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> update(@RequestBody RerouteParcelRequestDto requestDto) {
        final RerouteParcelRequest request = requestMapper.map(requestDto);
        final RerouteParcelResponse updateResponse = rerouteUpdatePort.update(request);
        return new ResponseEntity<>(responseMapper.map(updateResponse), HttpStatus.OK);
    }

    @GetMapping("/token/{value}")
    public ResponseEntity<?> getToken(Token token) {
        final RerouteToken rerouteToken = port.findByToken(token);
        return ResponseEntity.ok(rerouteToken);
    }

    @GetMapping("/token/{value}/parcel/{parcelId}")
    public ResponseEntity<?> loadByTokenAndParcelId(Integer token, Long parcelId) {
        final RerouteToken rerouteToken = port.loadByTokenAndParcelId(token, parcelId);
        return ResponseEntity.ok(rerouteToken);
    }

    @GetMapping("/valid/token/{value}/parcel/{parcelId}")
    public ResponseEntity<?> isTokenValid(Integer token, Long parcelId) {
        final boolean isValid = port.loadByTokenAndParcelId(token, parcelId).isValid();
        return ResponseEntity.ok(isValid);
    }

}
