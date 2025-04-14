package com.warehouse.deliverymissed.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPortImpl;
import com.warehouse.deliverymissed.domain.port.secondary.*;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.domain.service.SuggestedActionDetermineService;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.*;
import com.warehouse.tools.parcelstatus.ShipmentStatusProperties;

@Configuration
public class DeliveryMissedConfiguration {

	@Bean
	public DeliveryMissedPort deliveryMissedPort(final DeliveryMissedService deliveryMissedService,
												 final DeliveryInstructionServicePort deliveryInstructionServicePort,
												 final SuggestedActionDetermineService suggestedActionDetermineService) {
		return new DeliveryMissedPortImpl(deliveryMissedService, deliveryInstructionServicePort,
				suggestedActionDetermineService);
	}

	@Bean
	public DeliveryInstructionServicePort deliveryInstructionServicePort() {
		return new DeliveryInstructionServiceAdapter();
	}

	@Bean
	public DeliveryMissedService deliveryMissedService(DeliveryMissedRepository deliveryMissedRepository,
			ShipmentUpdateServicePort shipmentUpdateServicePort, final DeliveryMissedDetailsRepository detailsRepository) {
		return new DeliveryMissedServiceImpl(deliveryMissedRepository, shipmentUpdateServicePort, detailsRepository);
	}

	@Bean
	public ShipmentUpdateServicePort parcelStatusServicePort() {
		return new ShipmentUpdateServiceAdapter(parcelStatusProperties());
	}

	@Bean
	public DeliveryMissedDetailsRepository deliveryMissedDetailsRepository(
			final DeliveryMissedDetailsReadRepository deliveryMissedDetailsReadRepository) {
		return new DeliveryMissedDetailsRepositoryImpl(deliveryMissedDetailsReadRepository);
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
