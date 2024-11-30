package com.warehouse.deliverymissed.configuration;

import com.warehouse.deliverymissed.domain.port.secondary.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliverymissed.domain.port.primary.*;
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

	@Bean
	public SupplierValidatorPort supplierValidatorPort(SupplierRepository supplierRepository) {
		return new SupplierValidatorPortImpl(supplierRepository);
	}

	@Bean("deliveryMissed.supplierRepository")
	public SupplierRepository supplierRepository(
			@Qualifier("deliveryMissed.supplierReadRepository") SupplierReadRepository repository) {
		return new SupplierRepositoryImpl(repository);
	}
}
