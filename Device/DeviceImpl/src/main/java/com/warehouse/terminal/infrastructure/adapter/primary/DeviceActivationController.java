package com.warehouse.terminal.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.command.DeviceActivationCommand;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.vo.ActivationCode;
import com.warehouse.terminal.infrastructure.adapter.primary.mapper.TerminalResponseMapper;
import com.warehouse.terminal.request.DeviceActivationRequestDto;

@RestController
@RequestMapping("${api.exposure.internal-prefix}/device-activations")
public class DeviceActivationController {

    private final DevicePairPort devicePairPort;

    private final TerminalResponseMapper responseMapper = getMapper(TerminalResponseMapper.class);

    public DeviceActivationController(final DevicePairPort devicePairPort) {
        this.devicePairPort = devicePairPort;
    }

    @PostMapping
    public ResponseEntity<?> activate(@RequestBody final DeviceActivationRequestDto activationRequest) {
        final DeviceActivationCommand command = new DeviceActivationCommand(
                new ActivationCode(activationRequest.activationCode()),
                activationRequest.installationId(),
                StringUtils.isNotBlank(activationRequest.deviceType())
                        ? DeviceType.valueOf(activationRequest.deviceType().toUpperCase(Locale.ROOT))
                        : null,
                activationRequest.appVersion(),
                activationRequest.manufacturer(),
                activationRequest.model(),
                activationRequest.osName(),
                activationRequest.osVersion());
        return ResponseEntity.ok().body(responseMapper.map(this.devicePairPort.activate(command)));
    }
}
