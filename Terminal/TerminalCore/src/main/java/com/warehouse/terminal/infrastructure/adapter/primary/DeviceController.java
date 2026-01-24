package com.warehouse.terminal.infrastructure.adapter.primary;


import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.vo.DeviceTypeChangeCommand;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.dto.DeviceDto;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.request.DeviceTypeRequestDto;
import com.warehouse.terminal.request.DeviceUserRequestDto;
import com.warehouse.terminal.request.DeviceVersionRequestDto;
import com.warehouse.terminal.request.DeviceCreateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@RestController
@RequestMapping("/devices")
public class DeviceController implements DeviceApiService {

    private final DevicePort devicePort;

    private final TerminalRequestMapper requestMapper = getMapper(TerminalRequestMapper.class);

    private final TerminalResponseMapper responseMapper = getMapper(TerminalResponseMapper.class);

    public DeviceController(final DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    @PostMapping
    public ResponseEntity<?> addDevice(@RequestBody final DeviceCreateRequestDto deviceCreateRequest) {
        final DeviceCreateCommand command = null;
        this.devicePort.create(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/device-types")
    public ResponseEntity<?> changeDeviceType(@RequestBody final DeviceTypeRequestDto deviceTypeRequest) {
        final DeviceTypeChangeCommand command = null;
        this.devicePort.changeDeviceTypeTo(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users")
    public ResponseEntity<?> assignUser(@RequestBody final DeviceUserRequestDto deviceUserRequest) {
        final DeviceUserRequest request = null;
        this.devicePort.changeUserTo(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/versions")
    public ResponseEntity<?> changeVersion(@RequestBody final DeviceVersionRequestDto deviceVersionRequest) {
        final DeviceVersionRequest request = null;
        this.devicePort.changeVersionTo(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDevice(@PathVariable final Long id) {
        final DeviceId deviceId = new DeviceId(id);
        final Terminal device = this.devicePort.getDevice(deviceId);
        return ResponseEntity.ok(responseMapper.mapToDeviceResponse(device));
    }

    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        final List<Terminal> devices = this.devicePort.allDevices();
        final List<DeviceDto> deviceResponse = devices
                .stream()
                .map(responseMapper::mapToDeviceResponse)
                .toList();
        return ResponseEntity.ok().body(deviceResponse);
    }
}
