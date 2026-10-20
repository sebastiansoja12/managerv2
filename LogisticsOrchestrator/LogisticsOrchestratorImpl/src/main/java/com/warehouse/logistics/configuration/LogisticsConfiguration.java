package com.warehouse.logistics.configuration;

import com.warehouse.logistics.application.port.primary.DepartmentValidatorPort;
import com.warehouse.logistics.application.port.primary.DepartmentValidatorPortImpl;
import com.warehouse.logistics.application.port.primary.DeviceAgentPort;
import com.warehouse.logistics.application.port.primary.DeviceAgentPortImpl;
import com.warehouse.logistics.application.port.primary.DeviceValidatorPort;
import com.warehouse.logistics.application.port.primary.DeviceValidatorPortImpl;
import com.warehouse.logistics.application.port.primary.LogisticsPort;
import com.warehouse.logistics.application.port.primary.LogisticsPortImpl;
import com.warehouse.logistics.application.port.primary.PalletScanPort;
import com.warehouse.logistics.application.port.primary.PalletScanPortImpl;
import com.warehouse.logistics.application.port.primary.ProcessInitializerPort;
import com.warehouse.logistics.application.port.primary.ProcessInitializerPortImpl;
import com.warehouse.logistics.application.port.primary.ShipmentCollectPort;
import com.warehouse.logistics.application.port.primary.ShipmentCollectPortImpl;
import com.warehouse.logistics.application.port.primary.SupplierValidatorPort;
import com.warehouse.logistics.application.port.primary.SupplierValidatorPortImpl;
import com.warehouse.logistics.application.port.secondary.DeliveryCollectServicePort;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.application.port.secondary.DepartmentServicePort;
import com.warehouse.logistics.application.port.secondary.DeviceAgentServicePort;
import com.warehouse.logistics.application.port.secondary.PalletScanServicePort;
import com.warehouse.logistics.application.port.secondary.ProcessHubServicePort;
import com.warehouse.logistics.application.port.secondary.SupplierRepository;
import com.warehouse.logistics.application.service.LogisticsService;
import com.warehouse.logistics.application.service.LogisticsServiceImpl;
import com.warehouse.logistics.infrastructure.adapter.primary.DeviceAccessValidatorAspect;
import com.warehouse.logistics.infrastructure.adapter.primary.DeviceContextAuthenticator;
import com.warehouse.logistics.infrastructure.adapter.primary.DeviceTypeResolver;
import com.warehouse.logistics.infrastructure.adapter.primary.LoggingSoapEndpointExceptionResolver;
import com.warehouse.logistics.infrastructure.adapter.primary.LogisticsProcessFinishAspect;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.CollectMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.PalletScanMapper;
import com.warehouse.logistics.infrastructure.adapter.secondary.DeliveryCollectServiceAdapter;
import com.warehouse.logistics.infrastructure.adapter.secondary.DeliveryServiceAdapter;
import com.warehouse.logistics.infrastructure.adapter.secondary.DepartmentServiceAdapter;
import com.warehouse.logistics.infrastructure.adapter.secondary.DeviceAgentServiceAdapter;
import com.warehouse.logistics.infrastructure.adapter.secondary.PalletScanServiceAdapter;
import com.warehouse.logistics.infrastructure.adapter.secondary.ProcessHubServiceAdapter;
import com.warehouse.logistics.infrastructure.adapter.secondary.SupplierReadRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.SupplierRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.deliverycollect.api.DeliveryCollectApiService;
import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.palletmanagement.api.PalletApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.DeviceEventPublisher;
import com.warehouse.xmlconverter.XmlToStringService;
import com.warehouse.xmlconverter.XmlToStringServiceImpl;

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
    public DeviceTypeResolver deviceTypeResolver() {
        return new DeviceTypeResolver();
    }

    @Bean
    public DeviceAccessValidatorAspect deviceAccessValidatorAspect(
            final DeviceContextAuthenticator deviceContextAuthenticator,
            final DeviceTypeResolver deviceTypeResolver) {
        return new DeviceAccessValidatorAspect(deviceContextAuthenticator, deviceTypeResolver);
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
    public ProcessInitializerPort processInitializerPort(final ProcessHubServicePort processHubServicePort) {
        return new ProcessInitializerPortImpl(processHubServicePort);
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
	public LogisticsService logisticsService(final DeliveryServicePort deliveryServicePort,
                                             final DepartmentServicePort departmentServicePort,
                                             final OperatorContextProvider operatorContextProvider) {
		return new LogisticsServiceImpl(deliveryServicePort, departmentServicePort, operatorContextProvider);
	}

    @Bean("logistics.deliveryServicePort")
    public DeliveryServicePort deliveryServicePort(final DeliveryApiService deliveryApiService) {
        return new DeliveryServiceAdapter(deliveryApiService);
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
    public DepartmentValidatorPort departmentValidatorPort(final DepartmentServicePort departmentServicePort) {
        return new DepartmentValidatorPortImpl(departmentServicePort);
    }

    @Bean("logistics.departmentServicePort")
    public DepartmentServicePort departmentServicePort(final DepartmentApiService departmentApiService) {
        return new DepartmentServiceAdapter(departmentApiService);
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
    public PalletScanMapper palletScanMapper() {
        return new PalletScanMapper();
    }

    @Bean
    public PalletScanPort palletScanPort(final PalletScanServicePort palletScanServicePort) {
        return new PalletScanPortImpl(palletScanServicePort);
    }

    @Bean
    public PalletScanServicePort palletScanServicePort(final PalletApiService palletApiService,
                                                       final ProcessHubEventPublisher processHubEventPublisher) {
        return new PalletScanServiceAdapter(palletApiService, processHubEventPublisher);
    }

    @Bean
    public CollectMapper collectMapper() {
        return new CollectMapper();
    }

    @Bean
    public ShipmentCollectPort shipmentCollectPort(final DeliveryCollectServicePort deliveryCollectServicePort) {
        return new ShipmentCollectPortImpl(deliveryCollectServicePort);
    }

    @Bean
    public DeliveryCollectServicePort deliveryCollectServicePort(final DeliveryCollectApiService deliveryCollectApiService) {
        return new DeliveryCollectServiceAdapter(deliveryCollectApiService);
    }
}
