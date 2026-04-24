package com.warehouse.terminal.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.command.DevicePairRequest;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.vo.DeviceInformationResponse;
import com.warehouse.terminal.domain.vo.DevicePairResponse;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalRequestMapper;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.request.DevicePairRequestDto;

@RestController
@RequestMapping("/device-pairings")
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

    @GetMapping("/connected/{deviceId}")
    public ResponseEntity<?> isConnected(@PathVariable final String deviceId) {
        final DeviceId terminalId = new DeviceId(deviceId);
        final DeviceInformationResponse response = new DeviceInformationResponse(this.devicePairPort.isPaired(terminalId));
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

    @GetMapping("/update-required")
    public ResponseEntity<?> isUpdateRequired(@RequestParam("deviceId") final String deviceId,
                                               @RequestParam("version") final String version) {
        final DeviceId id = new DeviceId(deviceId);
        final DeviceVersion deviceVersion = new DeviceVersion(version, id);
        final DeviceInformationResponse response =
                new DeviceInformationResponse(this.devicePairPort.updateRequired(id, deviceVersion));
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

    @GetMapping("/version-valid")
    public ResponseEntity<?> isVersionValid(@RequestParam("deviceId") final String deviceId,
                                             @RequestParam("version") final String version) {
        final DeviceId id = new DeviceId(deviceId);
        final DeviceVersion deviceVersion = new DeviceVersion(version, id);
        final DeviceInformationResponse response =
                new DeviceInformationResponse(this.devicePairPort.isVersionValid(id, deviceVersion));
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

    @GetMapping("/user-valid")
    public ResponseEntity<?> isUserValid(@RequestParam("deviceId") final String deviceId,
                                          @RequestParam("username") final String username) {
        final DeviceId id = new DeviceId(deviceId);
        final Username user = new Username(username);
        final DeviceInformationResponse response =
                new DeviceInformationResponse(this.devicePairPort.isUserValid(id, user));
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

    @GetMapping("/device-validation")
    public ResponseEntity<?> validate(@RequestParam("deviceId") final String deviceId) {
        final DeviceInformationResponse response =
                new DeviceInformationResponse(this.devicePairPort.isValid(new DeviceId(deviceId)));
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

    @GetMapping("/pair-key-validation")
    public ResponseEntity<?> validateByPairKey(@RequestParam("pairKey") final String pairKey) {
        final DeviceInformationResponse response =
                new DeviceInformationResponse(this.devicePairPort.isPairKeyValid(pairKey));
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

}
