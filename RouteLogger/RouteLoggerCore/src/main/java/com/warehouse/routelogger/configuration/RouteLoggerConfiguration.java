package com.warehouse.routelogger.configuration;


import com.warehouse.routelogger.domain.port.secondary.RouteLoggerTerminalServicePort;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerVersionServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLoggerTerminalIdServiceAdapter;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLoggerVersionServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPortImpl;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeliveryServicePort;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerSupplierCodeServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLoggerDeliveryServiceAdapter;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLoggerSupplierCodeServiceAdapter;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

@Configuration
public class RouteLoggerConfiguration {

	@Bean
	public RouteLoggerPort routeLoggerPort(RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort,
			RouteLoggerSupplierCodeServicePort routeLoggerSupplierCodeServicePort,
			RouteLoggerTerminalServicePort routeLoggerTerminalServicePort,
			RouteLoggerVersionServicePort routeLoggerVersionServicePort) {
		return new RouteLoggerPortImpl(routeLoggerDeliveryServicePort, routeLoggerSupplierCodeServicePort,
				routeLoggerTerminalServicePort, routeLoggerVersionServicePort);
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
