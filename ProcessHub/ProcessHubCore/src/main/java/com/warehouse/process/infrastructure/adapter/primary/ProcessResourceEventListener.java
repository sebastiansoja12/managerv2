package com.warehouse.process.infrastructure.adapter.primary;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.infrastructure.event.ProcessInitializeEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcessResourceEventListener {

    private final ProcessPort processPort;

    public ProcessResourceEventListener(final ProcessPort processPort) {
        this.processPort = processPort;
    }

    @EventListener
    public void handle(final ProcessInitializeEvent event) {

    }
}
