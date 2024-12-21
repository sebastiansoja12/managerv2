package com.warehouse.delivery.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.warehouse.delivery.domain.port.primary.*;
import com.warehouse.delivery.domain.port.secondary.*;
import com.warehouse.delivery.domain.service.DeliveryService;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.delivery.infrastructure.adapter.secondary.*;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;

@Configuration
public class DeliveryConfiguration {

    @Bean
    public DeliveryPort deliveryPort(final DeliveryService deliveryService,
                                     final RouteLogDeliveryStatusServicePort logServicePort) {
        return new DeliveryPortImpl(deliveryService, logServicePort);
    }

    @Bean
    public TerminalRequestLoggerPort terminalRequestLoggerPort(final DeliveryTrackerLogServicePort deliveryTrackerLogServicePort) {
        return new TerminalRequestLoggerPortImpl(deliveryTrackerLogServicePort);
    }

    @Bean
    public DeliveryTrackerLogServicePort deliveryTrackerLogServicePort(final RouteLogEventPublisher routeLogEventPublisher) {
        return new DeliveryTrackerLogServiceAdapter(routeLogEventPublisher);
    }

    @Bean("delivery.routeLogEventPublisher")
    @Primary
    public RouteLogEventPublisher routeLogEventPublisher(final ApplicationEventPublisher eventPublisher) {
        return new RouteLogEventPublisherImpl(eventPublisher);
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
    public RouteLogDeliveryStatusServicePort deliveryServicePort(RouteLogEventPublisher routeLogEventPublisher) {
        return new RouteLogDeliveryStatusAdapter(routeLogEventPublisher);
    }

    @Bean(name = "delivery.supplierTokenServicePort")
    public DeliveryTokenServicePort supplierTokenServicePort(DeliveryTokenService service) {
        return new DeliveryTokenAdapter(service);
    }

    @Bean
    public SupplierValidatorPort supplierValidatorPort(final SupplierRepository supplierRepository) {
        return new SupplierValidatorPortImpl(supplierRepository);
    }

    @Bean("delivery.supplierRepository")
    public SupplierRepository supplierRepository(final SupplierReadRepository repository) {
        return new SupplierRepositoryImpl(repository);
    }

    @Bean
    public DepartmentValidatorPort departmentValidatorPort(final DepartmentRepository repository) {
        return new DepartmentValidatorPortImpl(repository);
    }

    @Bean("delivery.departmentRepository")
    public DepartmentRepository departmentRepository(final DepartmentReadRepository repository) {
        return new DepartmentRepositoryImpl(repository);
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
