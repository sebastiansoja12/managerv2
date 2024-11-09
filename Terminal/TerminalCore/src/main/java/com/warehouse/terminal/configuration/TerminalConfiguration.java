package com.warehouse.terminal.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.terminal.domain.port.primary.TerminalPairPort;
import com.warehouse.terminal.domain.port.primary.TerminalPairPortImpl;

@Configuration
public class TerminalConfiguration {

    @Bean
    public TerminalPairPort terminalPairPort() {
        return new TerminalPairPortImpl();
    }
}
