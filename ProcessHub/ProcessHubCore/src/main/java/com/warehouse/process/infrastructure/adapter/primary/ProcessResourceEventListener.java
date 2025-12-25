package com.warehouse.process.infrastructure.adapter.primary;

import com.warehouse.process.infrastructure.event.ProcessInitializeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessResourceEventListener {


    @EventListener
    public void handle(final ProcessInitializeEvent event) {

    }
}
