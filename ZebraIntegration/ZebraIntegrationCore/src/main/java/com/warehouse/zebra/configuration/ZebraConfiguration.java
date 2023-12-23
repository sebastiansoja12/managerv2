package com.warehouse.zebra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebra.domain.port.primary.ZebraPort;
import com.warehouse.zebra.domain.port.primary.ZebraPortImpl;
import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.infrastructure.adapter.primary.ZebraAdapter;
import com.warehouse.zebra.infrastructure.adapter.secondary.ReturnServiceAdapter;
import com.warehouse.zebra.infrastructure.adapter.secondary.RouteLogServiceAdapter;

import jakarta.validation.constraints.NotNull;

@Configuration
public class ZebraConfiguration {

    @Bean
    public ZebraPort zebraPort(ReturnServicePort returnServicePort,  RouteLogServicePort routeLogServicePort) {
        return new ZebraPortImpl(returnServicePort, routeLogServicePort);
    }
    
	@Bean
	public RouteLogServicePort routeLogServicePort(@NotNull RouteTrackerLogProperties routeTrackerLogProperties) {
		return new RouteLogServiceAdapter(routeTrackerLogProperties);
	}

    @Bean("zebra.returnProperties")
    public ReturnProperties returnProperties() {
        return new ReturnProperties();
    }

    @Bean("zebra.routeTrackerLogProperties")
    public RouteTrackerLogProperties routeTrackerLogProperties() {
        return new RouteTrackerLogProperties();
    }

    @Bean
    public ReturnServicePort returnServicePort(ReturnProperties returnProperties) {
        return new ReturnServiceAdapter(returnProperties);
    }

    @Bean
    public ZebraAdapter zebraAdapter(ZebraPort zebraPort) {
        return new ZebraAdapter(zebraPort);
    }
}
