package com.warehouse.terminal.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.port.primary.TerminalPort;
import com.warehouse.terminal.domain.vo.DeviceTypeRequest;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.dto.DeviceDto;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.request.DeviceTypeRequestDto;
import com.warehouse.terminal.request.DeviceUserRequestDto;
import com.warehouse.terminal.request.DeviceVersionRequestDto;
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
        final TerminalAddRequest request = requestMapper.map(terminalAddRequest);
        this.terminalPort.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/device-type")
    public ResponseEntity<?> changeDeviceType(@RequestBody final DeviceTypeRequestDto deviceTypeRequest) {
        final DeviceTypeRequest request = requestMapper.map(deviceTypeRequest);
        this.terminalPort.changeDeviceTypeTo(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user")
    public ResponseEntity<?> assignUser(@RequestBody final DeviceUserRequestDto deviceUserRequest) {
        final DeviceUserRequest request = requestMapper.map(deviceUserRequest);
        this.terminalPort.changeUserTo(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/version")
    public ResponseEntity<?> changeVersion(@RequestBody final DeviceVersionRequestDto deviceVersionRequest) {
        final DeviceVersionRequest request = requestMapper.map(deviceVersionRequest);
        this.terminalPort.changeVersionTo(request);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        final List<Terminal> devices = this.terminalPort.allDevices();
        final List<DeviceDto> deviceResponse = devices
                .stream()
                .map(responseMapper::mapToDeviceResponse)
                .toList();
        return ResponseEntity.ok().body(deviceResponse);
    }
}
