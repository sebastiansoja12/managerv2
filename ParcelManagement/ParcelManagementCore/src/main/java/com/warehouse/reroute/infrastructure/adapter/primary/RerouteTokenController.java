package com.warehouse.reroute.infrastructure.adapter.primary;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.port.primary.RerouteServicePort;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelResponse;
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

    @PostMapping()
    ParcelResponse update(@RequestBody UpdateParcelRequest request) {
        return port.update(request);
    }

    @GetMapping("/token/{value}")
    RerouteTokenResponse getToken(Token token) {
        return port.findByToken(token);
    }

    @GetMapping("/token/{value}/parcel/{parcelId}")
    RerouteTokenResponse loadByTokenAndParcelId(Token token, ParcelId parcel) {
        return port.loadByTokenAndParcelId(token, parcel);
    }

    @GetMapping("/valid/token/{value}/parcel/{parcelId}")
    boolean isTokenValid(Token token, ParcelId parcel) {
        return port.loadByTokenAndParcelId(token, parcel).isValid();
    }

}
