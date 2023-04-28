package com.warehouse.reroute.infrastructure.adapter.primary;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.primary.RerouteServicePort;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reroutes")
@AllArgsConstructor
public class RerouteTokenController {

    private final RerouteServicePort port;

    @PostMapping("/information")
    RerouteResponse sendInformation(@RequestBody RerouteRequest request) {
        return port.sendReroutingInformation(request);
    }

    @PostMapping
    ParcelUpdateResponse update(@RequestBody UpdateParcelRequest request) {
        return port.update(request);
    }

    @GetMapping("/token/{value}")
    RerouteToken getToken(Token token) {
        return port.findByToken(token);
    }

    @GetMapping("/token/{value}/parcel/{parcelId}")
    RerouteToken loadByTokenAndParcelId(Token token, ParcelId parcel) {
        return port.loadByTokenAndParcelId(token, parcel);
    }

    @GetMapping("/valid/token/{value}/parcel/{parcelId}")
    boolean isTokenValid(Token token, ParcelId parcel) {
        return port.loadByTokenAndParcelId(token, parcel).isValid();
    }

}
