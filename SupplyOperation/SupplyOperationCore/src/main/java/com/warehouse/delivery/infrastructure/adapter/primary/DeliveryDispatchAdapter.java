package com.warehouse.delivery.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.delivery.domain.vo.DeviceResponse;
import com.warehouse.delivery.domain.vo.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/deliveries")
public class DeliveryDispatchAdapter extends ProcessDispatcher {

    private final DeliveryPort deliveryPort;

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final DeliveryRequestMapper requestMapper = getMapper(DeliveryRequestMapper.class);

    private final DeliveryResponseMapper responseMapper = getMapper(DeliveryResponseMapper.class);

    public DeliveryDispatchAdapter(final List<ProcessHandler> handlers, final DeliveryPort deliveryPort,
                                   final TerminalRequestLoggerPort terminalRequestLoggerPort) {
        super(handlers);
        this.deliveryPort = deliveryPort;
        this.terminalRequestLoggerPort = terminalRequestLoggerPort;
    }

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> processRequest(@RequestBody final TerminalRequest terminalRequest) {

        final Device device = terminalRequest.getDevice();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                device.getDeviceId(), device.getVersion(),
                device.getUsername(), device.getDepartmentCode());

        logTerminalRequest(terminalRequest);

        logDeviceId(terminalRequest);

        logVersion(terminalRequest);

        final DeliveryRequest deliveryRequest = this.requestMapper.map(terminalRequest);

        final DeliveryResponse deliveryResponse = this.process(deliveryRequest);

        final DeviceResponse deviceResponse = null;

        final Response response = null;

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    private void logDeviceId(final TerminalRequest terminalRequest) {
        log.info("Logging terminal id in tracker");
        terminalRequestLoggerPort.logDeviceId(terminalRequest);
    }

    private void logTerminalRequest(final TerminalRequest terminalRequest) {
        log.info("Logging request in tracker");
        terminalRequestLoggerPort.logRequest(terminalRequest);
    }

    private void logVersion(final TerminalRequest terminalRequest) {
        log.info("Logging device version in tracker");
        terminalRequestLoggerPort.logVersion(terminalRequest);
    }
}
