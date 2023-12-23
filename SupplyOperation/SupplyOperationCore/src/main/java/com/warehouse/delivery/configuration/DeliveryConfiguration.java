package com.warehouse.delivery.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.domain.port.primary.DeliveryPortImpl;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.delivery.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.delivery.infrastructure.adapter.secondary.*;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;

@Configuration
public class DeliveryConfiguration {

	@Bean
	public DeliveryPort deliveryPort(DeliveryService service, RouteLogServicePort logServicePort,
			ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort) {
		return new DeliveryPortImpl(service, logServicePort, parcelStatusControlChangeServicePort);
	}
    
	@Bean("delivery.parcelStatusControlChangeServicePort")
	public ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort(
			ParcelStatusProperties parcelStatusProperties) {
		return new ParcelStatusControlChangeServiceAdapter(parcelStatusProperties);
	}

	@Bean
	public DeliveryService deliveryService(DeliveryRepository deliveryRepository,
			DeliveryTokenServicePort servicePort) {
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

    @Bean(name = "delivery.supplierTokenServicePort")
    public DeliveryTokenServicePort supplierTokenServicePort(DeliveryTokenService service) {
        return new DeliveryTokenAdapter(service);
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
