package com.warehouse.terminal.domain.listener;

import com.warehouse.terminal.domain.event.DeviceCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.terminal.domain.service.DeviceVersionService;

@Component
public class DomainDeviceListener {

    private final DeviceVersionService deviceVersionService;

    public DomainDeviceListener(final DeviceVersionService deviceVersionService) {
        this.deviceVersionService = deviceVersionService;
    }

    @EventListener
    public void handle(final DeviceCreated event) {
        this.deviceVersionService.saveOrUpdate(event.getDeviceId(), event.getVersion());
    }
}
