package com.warehouse.routelogger.configuration;


import com.warehouse.routelogger.domain.port.secondary.*;
import com.warehouse.routelogger.infrastructure.adapter.secondary.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPortImpl;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

@Configuration
public class RouteLoggerConfiguration {

	@Bean
	public RouteLoggerPort routeLoggerPort(final RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort,
										   final RouteLoggerSupplierCodeServicePort routeLoggerSupplierCodeServicePort,
										   final RouteLoggerTerminalServicePort routeLoggerTerminalServicePort,
										   final RouteLoggerVersionServicePort routeLoggerVersionServicePort,
										   final RouteLoggerUsernameServicePort routeLoggerUsernameServicePort) {
		return new RouteLoggerPortImpl(routeLoggerDeliveryServicePort, routeLoggerSupplierCodeServicePort,
				routeLoggerTerminalServicePort, routeLoggerVersionServicePort, routeLoggerUsernameServicePort);
	}

	@Bean
	public RouteLoggerUsernameServicePort routeLoggerUsernameServicePort() {
		return new RouteLoggerUsernameServiceAdapter(routeTrackerLogProperties());
	}

	@Bean
	public RouteLoggerVersionServicePort routeLoggerVersionServicePort() {
		return new RouteLoggerVersionServiceAdapter(routeTrackerLogProperties());
	}

	@Bean
	public RouteLoggerTerminalServicePort routeLoggerTerminalServicePort() {
		return new RouteLoggerTerminalIdServiceAdapter(routeTrackerLogProperties());
	}
	
	@Bean
	public RouteLoggerSupplierCodeServicePort routeLoggerSupplierCodeServicePort() {
		return new RouteLoggerSupplierCodeServiceAdapter(routeTrackerLogProperties());
	}

	@Bean
	public RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort() {
		return new RouteLoggerDeliveryServiceAdapter(routeTrackerLogProperties());
	}

	@Bean("routelogger.routeTrackerLogProperties")
	public RouteTrackerLogProperties routeTrackerLogProperties() {
		return new RouteTrackerLogProperties();
	}
}
