package com.warehouse.terminal.domain.listener;

import com.warehouse.terminal.domain.event.DeviceChanged;
import com.warehouse.terminal.domain.vo.DeviceSnapshot;
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
    public void handle(final DeviceChanged event) {
        final DeviceSnapshot snapshot = event.getSnapshot();
        if (snapshot.deviceId() != null && snapshot.version() != null) {
            this.deviceVersionService.saveOrUpdate(snapshot.deviceId(), snapshot.version());
        }
    }
}
