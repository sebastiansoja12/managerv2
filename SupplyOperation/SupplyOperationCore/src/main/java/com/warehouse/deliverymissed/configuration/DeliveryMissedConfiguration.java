package com.warehouse.deliverymissed.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPortImpl;
import com.warehouse.deliverymissed.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.deliverymissed.domain.port.primary.TerminalRequestLoggerPortImpl;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryInstructionServicePort;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.port.secondary.ParcelStatusServicePort;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.*;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;

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
			ParcelStatusServicePort parcelStatusServicePort) {
		return new DeliveryMissedServiceImpl(deliveryMissedRepository, parcelStatusServicePort);
	}

	@Bean
	public ParcelStatusServicePort parcelStatusServicePort() {
		return new ParcelStatusServiceAdapter(parcelStatusProperties());
	}

	@Bean("deliveryMissed.parcelStatusProperties")
	public ParcelStatusProperties parcelStatusProperties() {
		return new ParcelStatusProperties();
	}

	@Bean
	public DeliveryMissedRepository deliveryMissedRepository(
			DeliveryMissedReadRepository deliveryMissedReadRepository) {
		return new DeliveryMissedRepositoryImpl(deliveryMissedReadRepository);
	}
}
