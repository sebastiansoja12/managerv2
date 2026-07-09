package com.warehouse.terminal.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.vo.DeviceInformationResponse;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;

@RestController
@RequestMapping("${api.exposure.internal-prefix}/device-pairing-verifications")
public class DevicePairVerificationController {

    private final DevicePairPort devicePairPort;

    private final TerminalResponseMapper responseMapper = getMapper(TerminalResponseMapper.class);

    public DevicePairVerificationController(final DevicePairPort devicePairPort) {
        this.devicePairPort = devicePairPort;
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
    public ResponseEntity<?> validateByPairKey() {
        final DeviceInformationResponse response = new DeviceInformationResponse(true);
        return ResponseEntity.ok().body(responseMapper.map(response));
    }

    @GetMapping("/paired")
    public ResponseEntity<?> isPairedByPairKey() {
        final DeviceInformationResponse response = new DeviceInformationResponse(true);
        return ResponseEntity.ok().body(responseMapper.map(response));
    }
}
