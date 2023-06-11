package com.warehouse.reroute.infrastructure.adapter.primary;


import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.port.primary.RerouteServicePort;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.PrimaryRequestMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.PrimaryResponseMapper;
import com.warehouse.reroute.infrastructure.api.RerouteApiService;
import com.warehouse.reroute.infrastructure.api.dto.RerouteRequestDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenServiceAdapter implements RerouteApiService {

    private final RerouteServicePort rerouteServicePort;

    private final PrimaryRequestMapper primaryRequestMapper;

    private final PrimaryResponseMapper primaryResponseMapper;

    @Override
    public RerouteResponseDto sendReroutingInformation(RerouteRequestDto requestDto) {
        final RerouteRequest request = primaryRequestMapper.map(requestDto);
        final RerouteResponse response = rerouteServicePort.sendReroutingInformation(request);
        return primaryResponseMapper.map(response);
    }
}
