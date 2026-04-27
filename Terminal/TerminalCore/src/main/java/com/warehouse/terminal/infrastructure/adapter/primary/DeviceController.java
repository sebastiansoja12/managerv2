package com.warehouse.terminal.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;
import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.command.*;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.dto.DeviceDto;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.validation.DeviceRequestValidationService;
import com.warehouse.terminal.request.*;

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

    @PutMapping("/{id}/device-types")
    public ResponseEntity<?> updateDeviceType(@PathVariable final String id,
                                              @RequestParam("value") final String value) {
        this.devicePort.updateDeviceType(
                new DeviceTypeUpdateCommand(new DeviceId(id), DeviceType.valueOf(value.toUpperCase(Locale.ROOT))));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/statuses")
    public ResponseEntity<?> updateStatus(@PathVariable final String id,
                                          @RequestParam("value") final String value) {
        this.devicePort.updateStatusField(
                new DeviceStatusUpdateCommand(new DeviceId(id), DeviceStatus.valueOf(value.toUpperCase(Locale.ROOT))));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/identities")
    public ResponseEntity<?> updateIdentity(@PathVariable final String id,
                                            @RequestBody final DeviceIdentityRequestDto request) {
        this.devicePort.updateIdentityField(new DeviceIdentityUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/hardwares")
    public ResponseEntity<?> updateHardware(@PathVariable final String id,
                                            @RequestBody final DeviceHardwareRequestDto request) {
        this.devicePort.updateHardwareField(new DeviceHardwareUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/softwares")
    public ResponseEntity<?> updateSoftware(@PathVariable final String id,
                                            @RequestBody final DeviceSoftwareRequestDto request) {
        this.devicePort.updateSoftwareField(new DeviceSoftwareUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/networks")
    public ResponseEntity<?> updateNetwork(@PathVariable final String id,
                                           @RequestBody final DeviceNetworkRequestDto request) {
        this.devicePort.updateNetworkField(new DeviceNetworkUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/securities")
    public ResponseEntity<?> updateSecurity(@PathVariable final String id,
                                            @RequestBody final DeviceSecurityRequestDto request) {
        this.devicePort.updateSecurityField(new DeviceSecurityUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/locations")
    public ResponseEntity<?> updateLocation(@PathVariable final String id,
                                            @RequestBody final DeviceLocationRequestDto request) {
        this.devicePort.updateLocationField(new DeviceLocationUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/ownerships")
    public ResponseEntity<?> updateOwnership(@PathVariable final String id,
                                             @RequestBody final DeviceOwnershipRequestDto request) {
        this.devicePort.updateOwnershipField(new DeviceOwnershipUpdateCommand(new DeviceId(id), requestMapper.map(request)));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/versions")
    public ResponseEntity<?> updateVersion(@PathVariable final String id,
                                           @RequestParam("value") final String value) {
        this.devicePort.updateVersionField(new DeviceVersionUpdateCommand(new DeviceId(id), value));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDevice(@PathVariable final String id) {
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
