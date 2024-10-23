package com.warehouse.tracking.configuration;

import com.warehouse.redirect.RedirectService;
import com.warehouse.reroute.RerouteService;
import com.warehouse.redirect.infrastructure.adapter.primary.RedirectServiceAdapter;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.infrastructure.adapter.primary.RerouteServiceAdapter;
import com.warehouse.tracking.TrackingStatusEventPublisher;
import com.warehouse.tracking.infrastructure.adapter.secondary.TrackingStatusEventPublisherImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingConfiguration {

    @Bean("tracking.rerouteService")
    public RerouteService rerouteService(final RerouteTokenPort rerouteTokenPort) {
        return new RerouteServiceAdapter(rerouteTokenPort);
    }

    @Bean("tracking.redirectService")
    public RedirectService redirectService() {
        return new RedirectServiceAdapter();
    }

    @Bean
    public TrackingStatusEventPublisher trackingStatusEventPublisher(final ApplicationEventPublisher eventPublisher) {
        return new TrackingStatusEventPublisherImpl(eventPublisher);
    }

}
