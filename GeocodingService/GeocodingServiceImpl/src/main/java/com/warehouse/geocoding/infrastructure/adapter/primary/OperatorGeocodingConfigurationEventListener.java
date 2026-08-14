package com.warehouse.geocoding.infrastructure.adapter.primary;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.geocoding.domain.port.primary.GeocodingPort;
import com.warehouse.geocoding.infrastructure.adapter.primary.mapper.GeocodingConfigurationApiMapper;
import com.warehouse.infrastructure.event.OperatorGeocodingConfigurationCreateEvent;

@Component
public class OperatorGeocodingConfigurationEventListener {

    private final GeocodingPort geocodingPort;

    public OperatorGeocodingConfigurationEventListener(final GeocodingPort geocodingPort) {
        this.geocodingPort = geocodingPort;
    }

    @EventListener
    public void handle(final OperatorGeocodingConfigurationCreateEvent event) {
        geocodingPort.create(GeocodingConfigurationApiMapper.toCreateCommand(event.configuration()));
    }
}
