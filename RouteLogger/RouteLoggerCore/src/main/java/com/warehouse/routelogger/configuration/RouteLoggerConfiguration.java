package com.warehouse.routelogger.configuration;


import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPortImpl;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeliveryServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLoggerDeliveryServiceAdapter;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteLoggerConfiguration {

	@Bean
	public RouteLoggerPort routeLoggerPort(RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort) {
		return new RouteLoggerPortImpl(routeLoggerDeliveryServicePort);
	}

	@Bean
	public RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort(
			RouteTrackerLogProperties routeTrackerLogProperties) {
		return new RouteLoggerDeliveryServiceAdapter(routeTrackerLogProperties);
	}

	@Bean("routelogger.routeTrackerLogProperties")
	public RouteTrackerLogProperties routeTrackerLogProperties() {
		return new RouteTrackerLogProperties();
	}
}
