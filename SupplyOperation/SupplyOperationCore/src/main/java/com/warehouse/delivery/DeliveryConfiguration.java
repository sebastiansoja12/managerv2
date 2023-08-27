package com.warehouse.delivery;

import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.service.DeliveryService;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryReadRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryRepositoryImpl;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.domain.port.primary.DeliveryPortImpl;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.RouteLogAdapter;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;

@Configuration
public class DeliveryConfiguration {

    @Bean
    public DeliveryPort deliveryPort(DeliveryService service) {
        return new DeliveryPortImpl(service);
    }
    
	@Bean
	public DeliveryService deliveryService(RouteLogServicePort routeLogServicePort,
			DeliveryRepository deliveryRepository) {
		return new DeliveryServiceImpl(routeLogServicePort, deliveryRepository);
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

    // Mappers
    @Bean
    public DeliveryRequestMapper requestMapper() {
        return Mappers.getMapper(DeliveryRequestMapper.class);
    }

    @Bean
    public DeliveryResponseMapper responseMapper() {
        return Mappers.getMapper(DeliveryResponseMapper.class);
    }
}
