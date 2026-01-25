package com.warehouse.terminal.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.vo.DeviceTypeChangeCommand;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.dto.DeviceDto;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.validation.DeviceRequestValidationService;
import com.warehouse.terminal.request.DeviceCreateRequestDto;
import com.warehouse.terminal.request.DeviceTypeRequestDto;
import com.warehouse.terminal.request.DeviceUpdateRequestDto;
import com.warehouse.terminal.request.DeviceUserRequestDto;
import com.warehouse.terminal.request.DeviceVersionRequestDto;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DevicePort devicePort;

    private final DeviceRequestValidationService requestValidationService;

    private final TerminalRequestMapper requestMapper = getMapper(TerminalRequestMapper.class);

    private final TerminalResponseMapper responseMapper = getMapper(TerminalResponseMapper.class);

    public DeviceController(final DevicePort devicePort,
                            final DeviceRequestValidationService requestValidationService) {
        this.devicePort = devicePort;
        this.requestValidationService = requestValidationService;
    }

    @PostMapping
    public ResponseEntity<?> addDevice(@RequestBody final DeviceCreateRequestDto deviceCreateRequest) {
        this.requestValidationService.validateCreateRequest(deviceCreateRequest);
        final DeviceCreateCommand command = requestMapper.map(deviceCreateRequest);
        this.devicePort.create(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/device-types")
    public ResponseEntity<?> changeDeviceType(@RequestBody final DeviceTypeRequestDto deviceTypeRequest) {
        final DeviceTypeChangeCommand command = requestMapper.map(deviceTypeRequest);
        this.devicePort.changeDeviceTypeTo(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users")
    public ResponseEntity<?> assignUser(@RequestBody final DeviceUserRequestDto deviceUserRequest) {
        final DeviceUserRequest request = requestMapper.map(deviceUserRequest);
        this.devicePort.changeUserTo(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/versions")
    public ResponseEntity<?> changeVersion(@RequestBody final DeviceVersionRequestDto deviceVersionRequest) {
        final DeviceVersionRequest request = requestMapper.map(deviceVersionRequest);
        this.devicePort.changeVersionTo(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profiles")
    public ResponseEntity<?> updateDevice(@RequestBody final DeviceUpdateRequestDto request) {
        this.requestValidationService.validateUpdateRequest(request);
        this.devicePort.updateDevice(requestMapper.map(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDevice(@PathVariable final Long id) {
        final DeviceId deviceId = new DeviceId(id);
        final Device device = this.devicePort.getDevice(deviceId);
        return ResponseEntity.ok(responseMapper.mapToDeviceResponse(device));
    }

    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        final List<Device> devices = this.devicePort.allDevices();
        final List<DeviceDto> deviceResponse = devices
                .stream()
                .map(responseMapper::mapToDeviceResponse)
                .toList();
        return ResponseEntity.ok().body(deviceResponse);
    }
}
