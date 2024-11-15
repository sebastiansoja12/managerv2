package com.warehouse.terminal.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.port.primary.TerminalPort;
import com.warehouse.terminal.dto.DeviceDto;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.request.TerminalAddRequestDto;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final TerminalPort terminalPort;

    private final TerminalRequestMapper requestMapper = getMapper(TerminalRequestMapper.class);
    private final TerminalResponseMapper responseMapper = getMapper(TerminalResponseMapper.class);

    public DeviceController(final TerminalPort terminalPort) {
        this.terminalPort = terminalPort;
    }

    @PostMapping
    public ResponseEntity<?> addDevice(@RequestBody final TerminalAddRequestDto terminalAddRequest) {
        final TerminalAddRequest request = TerminalAddRequest.from(terminalAddRequest);
        this.terminalPort.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        final List<Terminal> devices = this.terminalPort.allDevices();
        final List<DeviceDto> deviceResponse =
                devices.stream().map(responseMapper::mapToDeviceResponse).toList();
        return ResponseEntity.ok().body(deviceResponse);
    }
}
