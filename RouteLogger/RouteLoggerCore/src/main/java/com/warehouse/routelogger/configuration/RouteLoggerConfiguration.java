package com.warehouse.routelogger.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPortImpl;
import com.warehouse.routelogger.domain.port.secondary.*;
import com.warehouse.routelogger.infrastructure.adapter.secondary.*;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

@Configuration
public class RouteLoggerConfiguration {

	@Bean
	public RouteLoggerPort routeLoggerPort(final RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort,
			final RouteLoggerSupplierCodeServicePort routeLoggerSupplierCodeServicePort,
			final RouteLoggerTerminalServicePort routeLoggerTerminalServicePort,
			final RouteLoggerVersionServicePort routeLoggerVersionServicePort,
			final RouteLoggerUsernameServicePort routeLoggerUsernameServicePort,
			final RouteLoggerDepotCodeServicePort routeLoggerDepotCodeServicePort,
			final RouteLoggerRequestServicePort routeLoggerRequestServicePort,
			final RouteLoggerDeviceInformationServicePort routeLoggerDeviceInformationServicePort) {
		return new RouteLoggerPortImpl(routeLoggerDeliveryServicePort, routeLoggerSupplierCodeServicePort,
				routeLoggerTerminalServicePort, routeLoggerVersionServicePort, routeLoggerUsernameServicePort,
				routeLoggerDepotCodeServicePort, routeLoggerRequestServicePort, routeLoggerDeviceInformationServicePort);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "false")
	public RouteLoggerDeviceInformationServicePort routeLoggerDeviceInformationServicePort(final RouteTrackerLogProperties routeTrackerLogProperties) {
		return new RouteLoggerDeviceInformationServiceAdapter(routeTrackerLogProperties, retryTemplate());
	}

	@Bean
	public RetryTemplate retryTemplate() {
		return RetryTemplate.builder()
				.maxAttempts(3)
				.fixedBackoff(2000)
				.build();
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true")
	public RouteLoggerDeviceInformationServicePort routeLoggerDeviceInformationMockServicePort() {
		return new RouteLoggerDeviceInformationServiceMockAdapter();
	}
	
	@Bean
	public RouteLoggerRequestServicePort routeLoggerRequestServicePort() {
		return new RouteLoggerRequestServiceAdapter(routeTrackerLogProperties());
	}

	@Bean
	public RouteLoggerDepotCodeServicePort routeLoggerDepotCodeServicePort() {
		return new RouteLoggerDepotCodeServiceAdapter(routeTrackerLogProperties());
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
