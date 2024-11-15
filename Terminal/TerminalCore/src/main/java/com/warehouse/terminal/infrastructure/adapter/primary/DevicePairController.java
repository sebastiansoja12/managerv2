package com.warehouse.terminal.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.request.DevicePairRequest;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.model.response.DevicePairResponse;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.request.DevicePairRequestDto;
import com.warehouse.terminal.request.TerminalAddRequestDto;

@RestController
@RequestMapping("/terminals")
public class DevicePairController {

    private final DevicePairPort devicePairPort;

    private final TerminalRequestMapper requestMapper = getMapper(TerminalRequestMapper.class);

    public DevicePairController(final DevicePairPort devicePairPort) {
        this.devicePairPort = devicePairPort;
    }

    @PostMapping
    private ResponseEntity<?> addDevice(@RequestBody final TerminalAddRequestDto terminalAddRequest) {
        final TerminalAddRequest request = TerminalAddRequest.from(terminalAddRequest);
        //this.terminalPairPort.create(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pair")
    private ResponseEntity<?> pairDevice(@RequestBody final DevicePairRequestDto devicePairRequest) {
        final DevicePairRequest request = requestMapper.map(devicePairRequest);
        final DevicePairResponse response = this.devicePairPort.pair(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{deviceId}")
    private ResponseEntity<?> isConnected(@PathVariable final Long deviceId) {
        final DeviceId terminalId = new DeviceId(deviceId);
        return ResponseEntity.ok().body(this.devicePairPort.isConnected(terminalId));
    }

}
