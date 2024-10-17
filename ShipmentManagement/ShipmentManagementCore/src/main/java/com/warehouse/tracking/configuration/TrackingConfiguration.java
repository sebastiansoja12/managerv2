package com.warehouse.tracking.configuration;

import com.warehouse.redirect.RedirectService;
import com.warehouse.reroute.RerouteService;
import com.warehouse.redirect.infrastructure.adapter.primary.RedirectServiceAdapter;
import com.warehouse.reroute.infrastructure.adapter.primary.RerouteServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingConfiguration {

    @Bean
    public RerouteService rerouteService() {
        return new RerouteServiceAdapter();
    }

    @Bean
    public RedirectService redirectService() {
        return new RedirectServiceAdapter();
    }

}
