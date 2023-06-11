package com.warehouse.reroute.infrastructure.api;

import com.warehouse.reroute.infrastructure.api.dto.RerouteRequestDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteResponseDto;

public interface RerouteApiService {

    RerouteResponseDto sendReroutingInformation(RerouteRequestDto rerouteRequest);

}
