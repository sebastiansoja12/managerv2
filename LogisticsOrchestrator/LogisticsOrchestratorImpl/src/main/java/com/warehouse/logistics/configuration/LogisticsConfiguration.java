package com.warehouse.logistics.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.logistics.domain.port.primary.*;
import com.warehouse.logistics.domain.port.secondary.*;
import com.warehouse.logistics.domain.service.LogisticsService;
import com.warehouse.logistics.domain.service.LogisticsServiceImpl;
import com.warehouse.logistics.infrastructure.adapter.primary.DeviceAccessValidatorAspect;
import com.warehouse.logistics.infrastructure.adapter.primary.DeviceContextAuthenticator;
import com.warehouse.logistics.infrastructure.adapter.primary.LoggingSoapEndpointExceptionResolver;
import com.warehouse.logistics.infrastructure.adapter.primary.LogisticsProcessFinishAspect;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.logistics.infrastructure.adapter.secondary.*;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.DeviceEventPublisher;
import com.warehouse.xmlconverter.XmlToStringService;
import com.warehouse.xmlconverter.XmlToStringServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class LogisticsConfiguration {

    @Bean
    public LogisticsPort logisticsPort(final LogisticsService logisticsService) {
        return new LogisticsPortImpl(logisticsService);
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
    public DeviceContextAuthenticator deviceContextAuthenticator(final DeviceApiService deviceApiService) {
        return new DeviceContextAuthenticator(deviceApiService);
    }

    @Bean
    public DeviceAccessValidatorAspect deviceAccessValidatorAspect(
            final DeviceContextAuthenticator deviceContextAuthenticator) {
        return new DeviceAccessValidatorAspect(deviceContextAuthenticator);
    }

    @Bean
    public LogisticsProcessFinishAspect terminalResponseProcessFinishAspect(
            final ProcessHubEventPublisher processHubEventPublisher) {
        return new LogisticsProcessFinishAspect(processHubEventPublisher);
    }

    @Bean
    public LoggingSoapEndpointExceptionResolver loggingSoapEndpointExceptionResolver() {
        return new LoggingSoapEndpointExceptionResolver();
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
	public LogisticsService deliveryService(LogisticsRepository logisticsRepository,
                                            DeliveryTokenServicePort servicePort,
                                            DepartmentRepository departmentRepository,
                                            OperatorContextProvider operatorContextProvider) {
		return new LogisticsServiceImpl(logisticsRepository, servicePort, departmentRepository, operatorContextProvider);
	}

    @Bean
    public BaseRepository<DeliveryEntity> logisticsDeliveryEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public LogisticsRepository deliveryRepository(
            @Qualifier("logisticsDeliveryEntityRepository")
            final BaseRepository<DeliveryEntity> repository) {
        return new LogisticsRepositoryImpl(repository);
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
    public DepartmentRepository departmentRepository(final DepartmentApiService departmentApiService) {
        return new DepartmentRepositoryImpl(departmentApiService);
    }

    @Bean(name = "logistics.requestMapper")
    public LogisticsRequestMapper requestMapper() {
        return new LogisticsRequestMapper();
    }

    @Bean(name = "logistics.responseMapper")
    public LogisticsResponseMapper responseMapper() {
        return new LogisticsResponseMapper();
    }

    @Bean
    public DeliveryResponseMapper deliveryResponseMapper() {
        return new DeliveryResponseMapper();
    }
}
