package com.warehouse.logistics.configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.process.ProcessHubEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.warehouse.logistics.domain.port.primary.*;
import com.warehouse.logistics.domain.port.secondary.*;
import com.warehouse.logistics.domain.service.LogisticsService;
import com.warehouse.logistics.domain.service.LogisticsServiceImpl;
import com.warehouse.logistics.infrastructure.adapter.secondary.*;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;
import com.warehouse.terminal.DeviceEventPublisher;
import com.warehouse.xmlconverter.XmlToStringService;
import com.warehouse.xmlconverter.XmlToStringServiceImpl;

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
    public DeviceAgentServicePort deviceValidatorServicePort(final DeviceEventPublisher deviceEventPublisher,
                                                             final ProcessHubEventPublisher processHubEventPublisher) {
        return new DeviceAgentServiceAdapter(deviceEventPublisher, processHubEventPublisher);
    }

    @Bean
    public DeviceAgentPort deviceAgentPort(final DeviceAgentServicePort deviceAgentServicePort) {
        return new DeviceAgentPortImpl(deviceAgentServicePort);
    }

    @Bean
    public ProcessInitializerPort processInitializerPort(final ProcessHubServicePort processHubServicePort, final XmlToStringService xmlToStringService) {
        return new ProcessInitializerPortImpl(processHubServicePort, xmlToStringService);
    }

    @Bean
    public XmlToStringService xmlToStringService() {
        return new XmlToStringServiceImpl();
    }

    @Bean
    public ProcessHubServicePort processHubServicePort(final ProcessHubApiService processHubApiService,
                                                       final UserApiService userApiService) {
        return new ProcessHubServiceAdapter(processHubApiService, userApiService);
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
    public DeliveryTokenServicePort supplierTokenServicePort() {
        return new DeliveryTokenAdapter();
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
}
