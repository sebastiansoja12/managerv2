package com.warehouse.deliverymissed.configuration;

import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPortImpl;
import com.warehouse.deliverymissed.domain.port.primary.SupplierValidatorPort;
import com.warehouse.deliverymissed.domain.port.primary.SupplierValidatorPortImpl;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.port.secondary.SupplierRepository;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.*;
import com.warehouse.routelogger.RouteLogEventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryMissedConfiguration {

	@Bean
	public DeliveryMissedPort deliveryMissedPort(DeliveryMissedService deliveryMissedService,
			RouteLogMissedServicePort logMissedServicePort) {
		return new DeliveryMissedPortImpl(deliveryMissedService, logMissedServicePort);
	}

    @Bean
    public RouteLogMissedServicePort routeLogMissedServicePort(RouteLogEventPublisher routeLogEventPublisher) {
        return new RouteLogMissedServiceAdapter(routeLogEventPublisher);
    }
    
    @Bean
    public DeliveryMissedService deliveryMissedService(DeliveryMissedRepository deliveryMissedRepository) {
        return new DeliveryMissedServiceImpl(deliveryMissedRepository);
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
