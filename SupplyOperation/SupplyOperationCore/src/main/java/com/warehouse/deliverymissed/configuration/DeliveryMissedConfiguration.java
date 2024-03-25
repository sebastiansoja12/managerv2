package com.warehouse.deliverymissed.configuration;

import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPortImpl;
import com.warehouse.deliverymissed.domain.port.primary.SupplierValidatorPort;
import com.warehouse.deliverymissed.domain.port.primary.SupplierValidatorPortImpl;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.DeliveryMissedReadRepository;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.DeliveryMissedRepositoryImpl;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.RouteLogMissedServiceAdapter;
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
    public RouteLogMissedServicePort routeLogMissedServicePort() {
        return new RouteLogMissedServiceAdapter();
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
    public SupplierValidatorPort supplierValidatorPort() {
        return new SupplierValidatorPortImpl();
    }
}
