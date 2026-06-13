package com.warehouse.shipment.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.application.port.secondary.*;
import com.warehouse.shipment.application.service.*;
import com.warehouse.shipment.application.service.delivery.*;
import com.warehouse.shipment.application.service.returning.*;
import com.warehouse.shipment.application.service.status.*;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentApiServiceAdapter;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidatorImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OperatorShipmentConfigurationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.SignaturePersistenceMapper;
import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.voronoi.VoronoiService;

import java.util.List;


@Configuration
public class ShipmentConfiguration {

	private final LoggerFactory LOGGER_FACTORY = new LoggerFactoryImpl();

	@Bean(name = "shipment.mailPort")
	public MailPort mailPort(final com.warehouse.mail.domain.service.MailService mailService) {
		return new MailPortImpl(mailService);
	}

	@Bean
	public NotificationCreatorProvider notificationCreatorService() {
		return new NotificationCreatorProviderImpl();
	}

	@Bean(name = "shipment.departmentRepository")
	public DepartmentRepository departmentRepository(final DepartmentReadRepository repository) {
		return new DepartmentRepositoryImpl(repository);
	}

	@Bean
	public CountryServiceAvailabilityService countryServiceAvailabilityService(final DepartmentRepository departmentRepository) {
		return new CountryServiceAvailabilityServiceImpl(departmentRepository);
	}

	@Bean
	public PriceRepository priceRepository(final PriceReadRepository repository) {
		return new PriceRepositoryImpl(repository);
	}

	@Bean
	public CountryDetermineServicePort countryDetermineServicePort() {
		return new CountryDetermineServiceAdapter();
	}

	@Bean
	public ReturningServicePort returningServicePort(final ExternalFeignClient externalFeignClient,
													 final ReturnProperties returnProperties) {
		return new ReturningServiceClient(externalFeignClient, returnProperties);
	}

	@Bean
	public ShipmentPort shipmentPort(final ShipmentRepository shipmentRepository,
									 final SpecificationRepository specificationShipmentRepository,
									 final PathFinderServicePort pathFinderServicePort,
									 final PriceService priceService,
									 final CountryServiceAvailabilityService countryServiceAvailabilityService,
									 final SignatureService signatureService,
									 final RouteLogService routeLogService,
									 final ReturningServicePort returningServicePort,
									 final MailNotificationServicePort mailNotificationServicePort,
									 final TrackingNumberGenerationService trackingNumberGenerationService,
                                     final ShipmentConfigurationPort shipmentConfigurationServicePort,
                                     final OperatorContextProvider operatorContextProvider,
                                     final ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver,
                                     final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver,
                                     final ShipmentReturnStrategyResolver shipmentReturnStrategyResolver) {
        return new ShipmentPortImpl(shipmentRepository, specificationShipmentRepository,
				LOGGER_FACTORY.getLogger(ShipmentPortImpl.class), pathFinderServicePort, priceService,
				countryServiceAvailabilityService, signatureService, routeLogService, returningServicePort,
				mailNotificationServicePort, trackingNumberGenerationService,
				shipmentConfigurationServicePort,
                operatorContextProvider, shipmentDeliveryStrategyResolver, shipmentStatusChangeStrategyResolver,
                shipmentReturnStrategyResolver);
	}

    @Bean
    public ShipmentDeliveryStrategy shipmentDeliveredStrategy() {
        return new ShipmentDeliveredStrategy();
    }

    @Bean
    public ShipmentDeliveryStrategy shipmentReturnedStrategy() {
        return new ShipmentReturnedStrategy();
    }

    @Bean
    public ShipmentDeliveryStrategy shipmentRedirectedStrategy() {
        return new ShipmentRedirectedStrategy();
    }

    @Bean
    public ShipmentDeliveryStrategy shipmentSentStrategy() {
        return new ShipmentSentStrategy();
    }

    @Bean
    public ShipmentDeliveryStrategy shipmentUnchangedStrategy() {
        return new ShipmentUnchangedStrategy();
    }

    @Bean
    public ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver(
            final List<ShipmentDeliveryStrategy> strategies) {
        return new ShipmentDeliveryStrategyResolver(strategies);
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentCreatedStatusChangeStrategy() {
        return new ShipmentCreatedStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentRedirectedStatusChangeStrategy() {
        return new ShipmentRedirectedStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentReroutedStatusChangeStrategy() {
        return new ShipmentReroutedStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentSentStatusChangeStrategy() {
        return new ShipmentSentStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentDeliveredStatusChangeStrategy() {
        return new ShipmentDeliveredStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentReturnedStatusChangeStrategy() {
        return new ShipmentReturnedStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategy shipmentUnchangedStatusChangeStrategy() {
        return new ShipmentUnchangedStatusChangeStrategy();
    }

    @Bean
    public ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver(
            final List<ShipmentStatusChangeStrategy> strategies) {
        return new ShipmentStatusChangeStrategyResolver(strategies);
    }

    @Bean
    public ShipmentReturnStrategy shipmentReturnCreatedStrategy() {
        return new ShipmentReturnCreatedStrategy();
    }

    @Bean
    public ShipmentReturnStrategy shipmentReturnCompletedStrategy() {
        return new ShipmentReturnCompletedStrategy();
    }

    @Bean
    public ShipmentReturnStrategy shipmentReturnCancelledStrategy() {
        return new ShipmentReturnCancelledStrategy();
    }

    @Bean
    public ShipmentReturnStrategy shipmentReturnUnchangedStrategy() {
        return new ShipmentReturnUnchangedStrategy();
    }

    @Bean
    public ShipmentReturnStrategyResolver shipmentReturnStrategyResolver(
            final List<ShipmentReturnStrategy> strategies) {
        return new ShipmentReturnStrategyResolver(strategies);
    }

	@Bean
	public ShipmentConfigurationPort shipmentConfigurationServicePort(
			final OperatorConfigurationApiService operatorConfigurationApiService,
			final OperatorShipmentConfigurationMapper operatorShipmentConfigurationMapper) {
		return new ShipmentConfigurationServiceAdapter(operatorConfigurationApiService, operatorShipmentConfigurationMapper);
	}

	@Bean
	public OperatorShipmentConfigurationMapper operatorShipmentConfigurationMapper() {
		return new OperatorShipmentConfigurationMapper();
	}

	@Bean
	public ShipmentApiService shipmentApiService(final ShipmentPort shipmentPort) {
		return new ShipmentApiServiceAdapter(shipmentPort);
	}

	@Bean
	public TrackingSequenceRepository trackingSequenceRepository(final TrackingSequenceReadRepository repository) {
		return new TrackingSequenceRepositoryImpl(repository);
	}

	@Bean
	public TrackingNumberService trackingNumberService() {
		return new TrackingNumberServiceImpl();
	}

	@Bean
	public TrackingNumberSequenceService trackingNumberSequenceService(
			final TrackingSequenceRepository trackingSequenceRepository) {
		return new TrackingNumberSequenceService(trackingSequenceRepository);
	}

	@Bean
	public TrackingNumberGenerationService trackingNumberGenerationService(
			final TrackingNumberService trackingNumberService,
			final TrackingNumberSequenceService trackingNumberSequenceService) {
		return new TrackingNumberGenerationService(trackingNumberService, trackingNumberSequenceService);
	}
	
	@Bean
	public MailNotificationServicePort mailNotificationServicePort(
			final NotificationEventPublisher notificationEventPublisher) {
		return new MailNotificationServiceAdapter(notificationEventPublisher);
	}
	
	@Bean
	public CountryRepository countryRepository(final CountryReadRepository repository) {
		return new CountryRepositoryImpl(repository);
	}

	@Bean
	public SignatureService signatureService(final SignatureRepository signatureRepository,
											 final ShipmentRepository shipmentRepository) {
		return new SignatureServiceImpl(signatureRepository, shipmentRepository);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "false")
	public SignatureRepository signatureRepository(final SignatureReadRepository repository,
                                                   final SignaturePersistenceMapper persistenceMapper) {
		return new SignatureRepositoryImpl(repository, persistenceMapper);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true", matchIfMissing = true)
	public SignatureRepository signatureMockRepository(final SignaturePersistenceMapper persistenceMapper) {
		return new SignatureMockRepositoryImpl(persistenceMapper);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true", matchIfMissing = true)
	public RouteLogServicePort routeLogServiceMockPort() {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using mock Route log service port");
		return new RouteLogServiceMockAdapter();
	}

	@Bean(name = "shipment.routeLogServicePort")
	@ConditionalOnProperty(name = "services.mock", havingValue = "false")
	public RouteLogServicePort routeLogServicePort(final GenericFeignResourceService genericFeignResourceService,
											   final RouteTrackerLogProperties routeTrackerLogProperties,
											   final RouteLogRecordMapper routeLogRecordMapper) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Route log service port");
		return new RouteLogServiceClient(genericFeignResourceService, routeTrackerLogProperties,
				routeLogRecordMapper);
	}

	@Bean
	public GenericFeignClientFactory genericFeignClientFactory(final ObjectFactory<HttpMessageConverters> messageConverters,
															  final CurrentUserApiService currentUserApiService) {
		return new GenericFeignClientFactory(messageConverters, currentUserApiService);
	}

	@Bean
	public GenericFeignResourceService genericFeignResourceService(final GenericFeignClientFactory genericFeignClientFactory,
																  final ObjectMapper objectMapper) {
		return new GenericFeignResourceService(genericFeignClientFactory, objectMapper);
	}

	@Bean
	public DepartmentServicePort shipmentDepartmentServicePort(final DepartmentApiService departmentApiService) {
		return new DepartmentServiceClient(departmentApiService);
	}

	@Bean
	public UserServicePort shipmentUserServicePort(final UserApiService userApiService) {
		return new UserServiceClient(userApiService);
	}

	@Bean
	public RouteLogService routeLogService(final RouteLogServicePort routeLogServicePort,
										 final DepartmentServicePort departmentServicePort,
										 final UserServicePort userServicePort) {
		return new RouteLogServiceImpl(routeLogServicePort, departmentServicePort, userServicePort);
	}

	@Bean
	public RouteLogRecordMapper routeLogRecordMapper() {
		return new RouteLogRecordMapper();
	}

	@Bean
	public ShipmentRepository shipmentRepository(final OperatorFilteredRepository<ShipmentEntity> repository,
                                                 final ShipmentPersistenceMapper persistenceMapper) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Shipment repository");
		return new ShipmentRepositoryImpl(repository, persistenceMapper);
	}

	@Bean
	public ShipmentReadModelRepository shipmentReadModelRepository(
			final OperatorFilteredRepository<ShipmentReadEntity> repository,
            final ShipmentPersistenceMapper persistenceMapper) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Shipment read model repository");
		return new ShipmentReadModelRepositoryImpl(repository, persistenceMapper);
	}

	@Bean
	public ShipmentReadModelSyncService shipmentReadModelSyncService(
			final ShipmentReadModelRepository shipmentReadModelRepository,
			final ShipmentRepository shipmentRepository) {
		return new ShipmentReadModelSyncServiceImpl(shipmentReadModelRepository, shipmentRepository);
	}

	@Bean
	public ShipmentRequestMapper shipmentRequestMapper() {
		return Mappers.getMapper(ShipmentRequestMapper.class);
	}

	@Bean
	public ShipmentResponseMapper shipmentResponseMapper() {
		return Mappers.getMapper(ShipmentResponseMapper.class);
	}

	@Bean
	public ShipmentRequestValidator shipmentRequestValidator(final PriceService priceService) {
		return new ShipmentRequestValidatorImpl(priceService);
	}

	@Bean
	public SpecificationRepository specificationShipmentRepository(
			final OperatorFilteredRepository<ShipmentReadEntity> repository,
            final ShipmentPersistenceMapper persistenceMapper) {
		return new SpecificationShipmentRepositoryImpl(repository, persistenceMapper);
	}

    @Bean
    public ShipmentPersistenceMapper shipmentPersistenceMapper() {
        return new ShipmentPersistenceMapper();
    }

    @Bean
    public SignaturePersistenceMapper signaturePersistenceMapper() {
        return new SignaturePersistenceMapper();
    }

	@Bean("shipment.routeTrackerLogProperties")
	public RouteTrackerLogProperties routeTrackerLogProperties() {
		return new RouteTrackerLogProperties();
	}

	@Bean
	@ConditionalOnProperty(name="services.mock", havingValue="false")
	public PathFinderServicePort pathFinderServicePort(final VoronoiService voronoiService,
													   final DepartmentApiService departmentApiService) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using path finder service");
		return new PathFinderAdapter(voronoiService, departmentApiService);
	}

	//MOCK
	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true", matchIfMissing = true)
	public PathFinderServicePort pathFinderMockServicePort(final PathFinderMockService pathFinderMockService) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using mock path finder service");
		return new PathFinderMockAdapter(pathFinderMockService);
	}
}
