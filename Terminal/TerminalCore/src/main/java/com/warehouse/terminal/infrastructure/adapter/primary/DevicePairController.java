package com.warehouse.terminal.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.terminal.domain.model.command.DevicePairRequest;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.vo.DevicePairResponse;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.request.DevicePairRequestDto;

@RestController
@RequestMapping("${api.exposure.internal-prefix}/device-pairings")
public class DevicePairController {

    private final DevicePairPort devicePairPort;

    private final TerminalRequestMapper requestMapper = getMapper(TerminalRequestMapper.class);

    private final TerminalResponseMapper responseMapper = getMapper(TerminalResponseMapper.class);

    public DevicePairController(final DevicePairPort devicePairPort) {
        this.devicePairPort = devicePairPort;
    }

    @PostMapping
    public ResponseEntity<?> pairDevice(@RequestBody final DevicePairRequestDto devicePairRequest) {
        final DevicePairRequest request = requestMapper.map(devicePairRequest);
        final DevicePairResponse response = this.devicePairPort.pair(request);
        return ResponseEntity.ok().body(responseMapper.map(response));
    }
}
