package com.warehouse.logistics.configuration;

import com.warehouse.logistics.domain.port.primary.*;
import com.warehouse.logistics.domain.port.secondary.*;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.logistics.infrastructure.adapter.secondary.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import com.warehouse.logistics.domain.service.LogisticsService;
import com.warehouse.logistics.domain.service.LogisticsServiceImpl;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;
import com.warehouse.terminal.DeviceEventPublisher;

@Configuration
public class LogisticsConfiguration {

    @Bean
    public LogisticsPort logisticsPort(final LogisticsService logisticsService,
                                       final RouteLogDeliveryStatusServicePort logServicePort) {
        return new LogisticsPortImpl(logisticsService, logServicePort);
    }

    @Bean
    public DeviceValidatorPort deviceValidatorPort(final DeviceAgentServicePort deviceAgentServicePort) {
        return new DeviceValidatorPortImpl(deviceAgentServicePort);
    }

    @Bean
    public DeviceAgentServicePort deviceValidatorServicePort(final DeviceEventPublisher deviceEventPublisher) {
        return new DeviceAgentServiceAdapter(deviceEventPublisher);
    }

    @Bean
    public DeviceAgentPort deviceAgentPort(final DeviceAgentServicePort deviceAgentServicePort) {
        return new DeviceAgentPortImpl(deviceAgentServicePort);
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
	public LogisticsService deliveryService(LogisticsRepository logisticsRepository,
                                            DeliveryTokenServicePort servicePort) {
		return new LogisticsServiceImpl(logisticsRepository, servicePort);
	}

    @Bean
    public LogisticsRepository deliveryRepository(LogisticsReadRepository repository) {
        return new LogisticsRepositoryImpl(repository);
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
    public LogisticsRequestMapper requestMapper() {
        return Mappers.getMapper(LogisticsRequestMapper.class);
    }

    @Bean(name = "logistics.responseMapper")
    public LogisticsResponseMapper responseMapper() {
        return Mappers.getMapper(LogisticsResponseMapper.class);
    }
}
