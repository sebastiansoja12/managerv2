package com.warehouse.palletmanagement.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import com.warehouse.palletmanagement.application.service.PalletDocumentService;
import com.warehouse.palletmanagement.application.service.PalletGrossWeightService;
import com.warehouse.palletmanagement.application.service.PalletTransportService;
import com.warehouse.palletmanagement.application.port.secondary.CarrierServicePort;
import com.warehouse.palletmanagement.application.port.secondary.PalletTransportRepository;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.DriverHubCarrierServiceAdapter;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.PalletTransportRepositoryImpl;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.entity.PalletTransportEntity;
import com.warehouse.commonassets.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import com.warehouse.driverhub.api.CarrierApiService;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.palletmanagement.application.port.secondary.DepartmentServicePort;
import com.warehouse.palletmanagement.application.port.secondary.PalletDocumentRendererServicePort;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.PalletDepartmentServiceAdapter;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.document.PdfPalletDocumentServiceAdapter;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import org.springframework.beans.factory.annotation.Value;
import java.time.ZoneId;
import tools.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.resilience.CircuitBreakerExecutor;
import com.warehouse.palletmanagement.api.PalletApiService;
import com.warehouse.palletmanagement.application.port.primary.PalletPort;
import com.warehouse.palletmanagement.application.port.primary.PalletPortImpl;
import com.warehouse.palletmanagement.application.port.primary.PalletScanPort;
import com.warehouse.palletmanagement.application.port.primary.PalletScanPortImpl;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.PalletApiServiceAdapter;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletRequestMapper;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletResponseMapper;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.PalletHandlingManagerServerException;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.PalletHandlingManagerServiceClientAdapter;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.mapper.PalletHandlingManagerResponseMapper;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Configuration
public class PalletManagementConfiguration {

    @Bean
    public PalletPort palletPort(final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
                                 final ShipmentServicePort palletShipmentServicePort,
                                 final CarrierServicePort palletCarrierServicePort,
                                 final PalletTransportService palletTransportService,
                                 final PalletGrossWeightService palletGrossWeightService,
                                 final PalletDocumentService palletDocumentService) {
        return new PalletPortImpl(palletHandlingManagerServicePort, palletShipmentServicePort, palletCarrierServicePort,
                palletTransportService, palletGrossWeightService, palletDocumentService);
    }

    @Bean
    public PalletTransportService palletTransportService(final PalletTransportRepository palletTransportRepository) {
        return new PalletTransportService(palletTransportRepository);
    }

    @Bean
    public PalletGrossWeightService palletGrossWeightService(
            final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
            final ShipmentServicePort palletShipmentServicePort) {
        return new PalletGrossWeightService(palletHandlingManagerServicePort, palletShipmentServicePort);
    }

    @Bean
    public PalletDocumentService palletDocumentService(final ShipmentServicePort palletShipmentServicePort,
                                                       final DepartmentServicePort palletDepartmentServicePort,
                                                       final PalletDocumentRendererServicePort palletDocumentRendererServicePort) {
        return new PalletDocumentService(palletShipmentServicePort, palletDepartmentServicePort,
                palletDocumentRendererServicePort);
    }

    @Bean
    public CarrierServicePort palletCarrierServicePort(final CarrierApiService carrierApiService) {
        return new DriverHubCarrierServiceAdapter(carrierApiService);
    }

    @Bean
    public BaseRepository<PalletTransportEntity> palletTransportEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public PalletTransportRepository palletTransportRepository(
            @Qualifier("palletTransportEntityRepository") final BaseRepository<PalletTransportEntity> repository,
            final OperatorContextProvider operatorContextProvider,
            final CurrentUserApiService currentUserApiService) {
        return new PalletTransportRepositoryImpl(repository, operatorContextProvider, currentUserApiService);
    }

    @Bean
    public DepartmentServicePort palletDepartmentServicePort(final DepartmentApiService departmentApiService) {
        return new PalletDepartmentServiceAdapter(departmentApiService);
    }

    @Bean
    public PalletDocumentRendererServicePort palletDocumentRendererServicePort(
            @Value("${manager.pallet-documents.zone-id:Europe/Warsaw}") final String zoneId) {
        return new PdfPalletDocumentServiceAdapter(ZoneId.of(zoneId));
    }

    @Bean("palletManagement.palletScanPort")
    public PalletScanPort palletScanPort(final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
                                         final ShipmentServicePort palletShipmentServicePort,
                                         final DomainEventPublisher domainEventPublisher) {
        return new PalletScanPortImpl(palletHandlingManagerServicePort, palletShipmentServicePort,
                domainEventPublisher);
    }

    @Bean
    public PalletApiService palletApiService(final PalletScanPort palletScanPort,
                                             final PalletRequestMapper palletRequestMapper,
                                             final PalletResponseMapper palletResponseMapper) {
        return new PalletApiServiceAdapter(palletScanPort, palletRequestMapper, palletResponseMapper);
    }

    @Bean
    public PalletRequestMapper palletRequestMapper() {
        return new PalletRequestMapper();
    }

    @Bean
    public PalletResponseMapper palletResponseMapper() {
        return new PalletResponseMapper();
    }

    @Bean
    public ShipmentServicePort palletShipmentServicePort(final ShipmentApiService shipmentApiService) {
        return new ShipmentServiceAdapter(shipmentApiService);
    }

    @Bean
    public PalletHandlingManagerServicePort palletHandlingManagerServicePort(
            final RestClient.Builder restClientBuilder,
            final PalletHandlingManagerProperties properties,
            final CircuitBreakerRegistry circuitBreakerRegistry,
            final CircuitBreakerExecutor circuitBreakerExecutor,
            final CurrentUserApiService currentUserApiService,
            final ObjectMapper objectMapper) {
        circuitBreakerRegistry.circuitBreaker(PalletHandlingManagerServiceClientAdapter.CIRCUIT_BREAKER_NAME,
                circuitBreakerConfig(properties));

        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.connectTimeout());
        requestFactory.setReadTimeout(properties.readTimeout());

        final Retry readRetry = Retry.of(PalletHandlingManagerServiceClientAdapter.CIRCUIT_BREAKER_NAME + "-read",
                RetryConfig.custom()
                        .maxAttempts(properties.readMaxAttempts())
                        .waitDuration(properties.readRetryWait())
                        .retryExceptions(ResourceAccessException.class, PalletHandlingManagerServerException.class)
                        .build());

        return new PalletHandlingManagerServiceClientAdapter(
                restClientBuilder.clone().requestFactory(requestFactory),
                properties.url(),
                circuitBreakerExecutor,
                readRetry,
                currentUserApiService,
                objectMapper,
                new PalletHandlingManagerResponseMapper());
    }

    private CircuitBreakerConfig circuitBreakerConfig(final PalletHandlingManagerProperties properties) {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(properties.slidingWindowSize())
                .minimumNumberOfCalls(properties.minimumNumberOfCalls())
                .failureRateThreshold(properties.failureRateThreshold())
                .slowCallDurationThreshold(properties.slowCallDuration())
                .slowCallRateThreshold(properties.slowCallRateThreshold())
                .waitDurationInOpenState(properties.waitDurationInOpenState())
                .permittedNumberOfCallsInHalfOpenState(properties.permittedCallsInHalfOpenState())
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(ResourceAccessException.class, PalletHandlingManagerServerException.class)
                .build();
    }
}
