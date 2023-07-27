package com.warehouse.reroute.infrastructure.adapter.primary;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reroutes")
@AllArgsConstructor
public class RerouteTokenController {

    private final RerouteTokenPort port;

    @PostMapping("/information")
    public ResponseEntity<?> sendInformation(@RequestBody RerouteRequest request) {
        final RerouteResponse rerouteResponse = port.sendReroutingInformation(request);
        return ResponseEntity.ok(rerouteResponse);
    }

    @PostMapping
    public ResponseEntity<?> update(@RequestBody UpdateParcelRequest request) {
        final ParcelUpdateResponse updateResponse = port.update(request);
        return ResponseEntity.ok(updateResponse);
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
