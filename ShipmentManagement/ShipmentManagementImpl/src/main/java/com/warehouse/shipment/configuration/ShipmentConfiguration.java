package com.warehouse.shipment.configuration;

import java.util.Set;

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
import com.warehouse.shipment.domain.handler.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
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
import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.voronoi.VoronoiService;

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
	public ShipmentPort shipmentPort(final ShipmentService service,
									 final PathFinderServicePort pathFinderServicePort,
									 final NotificationCreatorProvider notificationCreatorProvider,
									 final Set<ShipmentStatusHandler> shipmentStatusHandlers,
									 final CountryDetermineService countryDetermineService,
									 final PriceService priceService,
									 final CountryServiceAvailabilityService countryServiceAvailabilityService,
									 final SignatureService signatureService,
									 final RouteLogService routeLogService,
									 final ReturningServicePort returningServicePort,
									 final MailNotificationServicePort mailNotificationServicePort,
									 final TrackingNumberService trackingNumberService,
									 final ShipmentConfigurationServicePort shipmentConfigurationServicePort,
                                     final OperatorContextProvider operatorContextProvider) {
		return new ShipmentPortImpl(service, LOGGER_FACTORY.getLogger(ShipmentPortImpl.class), pathFinderServicePort,
				notificationCreatorProvider, shipmentStatusHandlers, countryDetermineService, priceService,
				countryServiceAvailabilityService, signatureService, routeLogService, returningServicePort,
				mailNotificationServicePort, trackingNumberService, shipmentConfigurationServicePort,
                operatorContextProvider);
	}

	@Bean
	public ShipmentConfigurationServicePort shipmentConfigurationServicePort(
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
	public SignatureRepository signatureRepository(final SignatureReadRepository repository) {
		return new SignatureRepositoryImpl(repository);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true", matchIfMissing = true)
	public SignatureRepository signatureMockRepository() {
		return new SignatureMockRepositoryImpl();
	}

	@Bean
	public Set<ShipmentStatusHandler> shipmentStatusHandlers(final ShipmentService service) {
		return Set.of(new ShipmentCreatedHandler(), new ShipmentRerouteHandler(service),
				new ShipmentSentHandler(service), new ShipmentDeliveryHandler(service),
				new ShipmentRedirectHandler(service), new ShipmentReturnHandler(service));
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
	public ShipmentRepository shipmentRepository(final OperatorFilteredRepository<ShipmentEntity> repository) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Shipment repository");
		return new ShipmentRepositoryImpl(repository);
	}

	@Bean
	public ShipmentReadModelRepository shipmentReadModelRepository(
			final OperatorFilteredRepository<ShipmentReadEntity> repository) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Shipment read model repository");
		return new ShipmentReadModelRepositoryImpl(repository);
	}

	@Bean
	public ShipmentReadModelSyncService shipmentReadModelSyncService(
			final ShipmentReadModelRepository shipmentReadModelRepository,
			final OperatorFilteredRepository<ShipmentEntity> shipmentRepository,
			final OperatorContext operatorContext) {
		return new ShipmentReadModelSyncServiceImpl(shipmentReadModelRepository, shipmentRepository, operatorContext);
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

	@Bean(name = "shipment.shipmentService")
	public ShipmentService shipmentService(final ShipmentRepository shipmentRepository,
										   final SpecificationRepository specificationShipmentRepository) {
		return new ShipmentServiceImpl(shipmentRepository, specificationShipmentRepository);
	}

	@Bean
	public SpecificationRepository specificationShipmentRepository(
			final OperatorFilteredRepository<ShipmentReadEntity> repository) {
		return new SpecificationShipmentRepositoryImpl(repository);
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
