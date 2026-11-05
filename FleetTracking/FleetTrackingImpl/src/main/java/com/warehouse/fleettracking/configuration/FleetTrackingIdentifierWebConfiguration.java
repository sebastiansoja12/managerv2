package com.warehouse.fleettracking.configuration;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class FleetTrackingIdentifierWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, TrackingSessionId.class, source -> new TrackingSessionId(Long.valueOf(source)));
    }
}
