package com.warehouse.delivery.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.domain.port.primary.DeliveryPortImpl;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.delivery.infrastructure.adapter.secondary.*;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;

@Configuration
public class DeliveryConfiguration {

	@Bean
	public DeliveryPort deliveryPort(DeliveryService service, RouteLogServicePort logServicePort) {
		return new DeliveryPortImpl(service, logServicePort);
	}

	@Bean
	public DeliveryService deliveryService(DeliveryRepository deliveryRepository,
			SupplierTokenServicePort servicePort) {
		return new DeliveryServiceImpl(deliveryRepository, servicePort);
	}

    @Bean
    public DeliveryRepository deliveryRepository(DeliveryReadRepository repository) {
        return new DeliveryRepositoryImpl(repository);
    }

    @Bean
    public RouteLogServicePort deliveryServicePort(RouteLogEventPublisher routeLogEventPublisher) {
        final DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);
        return new RouteLogAdapter(routeLogEventPublisher, deliveryMapper);
    }

    @Bean
    @ConditionalOnProperty(name="service.mock", havingValue="false")
    public SupplierTokenServicePort supplierTokenServicePort() {
        return new SupplierTokenAdapter();
    }

    @Bean
    @ConditionalOnProperty(name="service.mock", havingValue="true")
    public SupplierTokenServicePort supplierTokenMockServicePort() {
        final SupplierTokenMockGenerator mockGenerator = new SupplierTokenMockGeneratorImpl();
        return new SupplierTokenMockAdapter(mockGenerator);
    }

    // Mappers
    @Bean(name = "delivery.requestMapper")
    public DeliveryRequestMapper requestMapper() {
        return Mappers.getMapper(DeliveryRequestMapper.class);
    }

    @Bean(name = "delivery.responseMapper")
    public DeliveryResponseMapper responseMapper() {
        return Mappers.getMapper(DeliveryResponseMapper.class);
    }
}
