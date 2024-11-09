package com.warehouse.terminal.infrastructure.adapter.primary;


import com.warehouse.terminal.domain.port.primary.TerminalPairPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok().build();
    }


    @PostMapping("/pair")
    private ResponseEntity<?> pairDevice(@RequestBody final TerminalPairRequestDto terminalPairRequest) {
        return ResponseEntity.ok().build();
    }

}
