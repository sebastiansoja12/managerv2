package com.warehouse.deliverymissed.configuration;

import com.warehouse.deliverymissed.domain.port.secondary.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPortImpl;
import com.warehouse.deliverymissed.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.deliverymissed.domain.port.primary.TerminalRequestLoggerPortImpl;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.*;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.tools.parcelstatus.ShipmentStatusProperties;

@Configuration
public class DeliveryMissedConfiguration {

	@Bean
	public DeliveryMissedPort deliveryMissedPort(final DeliveryMissedService deliveryMissedService,
												 final RouteLogMissedServicePort logMissedServicePort,
												 final DeliveryInstructionServicePort deliveryInstructionServicePort) {
		return new DeliveryMissedPortImpl(deliveryMissedService, logMissedServicePort, deliveryInstructionServicePort);
	}

	@Bean
	public DeliveryInstructionServicePort deliveryInstructionServicePort() {
		return new DeliveryInstructionServiceAdapter();
	}

	@Bean("deliveryMissed.terminalRequestLoggerPort")
	public TerminalRequestLoggerPort terminalRequestLoggerPort(RouteLogMissedServicePort routeLogMissedServicePort) {
		return new TerminalRequestLoggerPortImpl(routeLogMissedServicePort);
	}

	@Bean
	public RouteLogMissedServicePort routeLogMissedServicePort(RouteLogEventPublisher routeLogEventPublisher) {
		return new RouteLogMissedServiceAdapter(routeLogEventPublisher);
	}

	@Bean
	public DeliveryMissedService deliveryMissedService(DeliveryMissedRepository deliveryMissedRepository,
			ShipmentUpdateServicePort shipmentUpdateServicePort) {
		return new DeliveryMissedServiceImpl(deliveryMissedRepository, shipmentUpdateServicePort);
	}

	@Bean
	public ShipmentUpdateServicePort parcelStatusServicePort() {
		return new ShipmentUpdateServiceAdapter(parcelStatusProperties());
	}

	@Bean
	public DeliveryMissedDetailsRepository deliveryMissedDetailsRepository() {
		return new DeliveryMissedDetailsRepositoryImpl();
	}

	@Bean("deliveryMissed.parcelStatusProperties")
	public ShipmentStatusProperties parcelStatusProperties() {
		return new ShipmentStatusProperties();
	}

	@Bean
	public DeliveryMissedRepository deliveryMissedRepository(
			DeliveryMissedReadRepository deliveryMissedReadRepository) {
		return new DeliveryMissedRepositoryImpl(deliveryMissedReadRepository);
	}
}
