package com.warehouse.logistics.configuration;

import com.warehouse.logistics.domain.port.primary.*;
import com.warehouse.logistics.domain.port.secondary.*;
import com.warehouse.logistics.infrastructure.adapter.secondary.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import com.warehouse.logistics.domain.service.DeliveryService;
import com.warehouse.logistics.domain.service.DeliveryServiceImpl;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;
import com.warehouse.terminal.DeviceEventPublisher;

@Configuration
public class DeliveryConfiguration {

    @Bean
    public DeliveryPort deliveryPort(final DeliveryService deliveryService,
                                     final RouteLogDeliveryStatusServicePort logServicePort) {
        return new DeliveryPortImpl(deliveryService, logServicePort);
    }

    @Bean
    public DeviceValidatorPort deviceValidatorPort(final DeviceValidatorServicePort deviceValidatorServicePort) {
        return new DeviceValidatorPortImpl(deviceValidatorServicePort);
    }

    @Bean
    public DeviceValidatorServicePort deviceValidatorServicePort(final DeviceEventPublisher deviceEventPublisher) {
        return new DeviceValidatorServiceAdapter(deviceEventPublisher);
    }

    @Bean
    public TerminalRequestLoggerPort terminalRequestLoggerPort(final DeliveryTrackerLogServicePort deliveryTrackerLogServicePort) {
        return new TerminalRequestLoggerPortImpl(deliveryTrackerLogServicePort);
    }

    @Bean
    public DeliveryTrackerLogServicePort deliveryTrackerLogServicePort(final RouteLogEventPublisher routeLogEventPublisher) {
        return new DeliveryTrackerLogServiceAdapter(routeLogEventPublisher);
    }

    @Bean("logistics.routeLogEventPublisher")
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

    @Bean(name = "logistics.supplierTokenServicePort")
    public DeliveryTokenServicePort supplierTokenServicePort(DeliveryTokenService service) {
        return new DeliveryTokenAdapter(service);
    }

    @Bean
    public SupplierValidatorPort supplierValidatorPort(final SupplierRepository supplierRepository) {
        return new SupplierValidatorPortImpl(supplierRepository);
    }

    @Bean("logistics.supplierRepository")
    public SupplierRepository supplierRepository(final SupplierReadRepository repository) {
        return new SupplierRepositoryImpl(repository);
    }

    @Bean
    public DepartmentValidatorPort departmentValidatorPort(final DepartmentRepository repository) {
        return new DepartmentValidatorPortImpl(repository);
    }

    @Bean("logistics.departmentRepository")
    public DepartmentRepository departmentRepository(final DepartmentReadRepository repository) {
        return new DepartmentRepositoryImpl(repository);
    }

    // Mappers
    @Bean(name = "logistics.requestMapper")
    public DeliveryRequestMapper requestMapper() {
        return Mappers.getMapper(DeliveryRequestMapper.class);
    }

    @Bean(name = "logistics.responseMapper")
    public DeliveryResponseMapper responseMapper() {
        return Mappers.getMapper(DeliveryResponseMapper.class);
    }
}
