package com.warehouse.terminal.infrastructure.adapter.primary;


import com.warehouse.commonassets.identificator.TerminalId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.port.primary.TerminalPairPort;
import com.warehouse.terminal.request.TerminalAddRequestDto;
import com.warehouse.terminal.request.TerminalPairRequestDto;

@RestController
@RequestMapping("/terminals")
public class TerminalPairController {

    private final TerminalPairPort terminalPairPort;

    public TerminalPairController(final TerminalPairPort terminalPairPort) {
        this.terminalPairPort = terminalPairPort;
    }

    @PostMapping
    private ResponseEntity<?> addDevice(@RequestBody final TerminalAddRequestDto terminalAddRequest) {
        final TerminalAddRequest request = TerminalAddRequest.from(terminalAddRequest);
        this.terminalPairPort.create(request);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/pair")
    private ResponseEntity<?> pairDevice(@RequestBody final TerminalPairRequestDto terminalPairRequest) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{deviceId}")
    private ResponseEntity<?> isConnected(@PathVariable final Long deviceId) {
        final TerminalId terminalId = new TerminalId(deviceId);
        return ResponseEntity.ok().body(this.terminalPairPort.isConnected(terminalId));
    }

}
