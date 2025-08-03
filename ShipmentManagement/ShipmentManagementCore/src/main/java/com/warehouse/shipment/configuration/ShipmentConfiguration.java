package com.warehouse.shipment.configuration;

import java.time.Duration;
import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.shipment.domain.handler.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidatorImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.notifier.RouteTrackerHistoryNotifier;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingStatusEventPublisher;
import com.warehouse.voronoi.VoronoiService;

import io.github.resilience4j.retry.RetryConfig;

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

	@Bean
	public CountryServiceAvailabilityService countryServiceAvailabilityService() {
		return new CountryServiceAvailabilityServiceImpl();
	}

	@Bean
	public PriceRepository priceRepository(final PriceReadRepository repository) {
		return new PriceRepositoryImpl(repository);
	}

	@Bean
	public RouteTrackerHistoryNotifier routeTrackerNotifier() {
		return new RouteTrackerHistoryNotifier();
	}

	@Bean
	public CountryDetermineServicePort countryDetermineServicePort() {
		return new CountryDetermineServiceAdapter();
	}

	@Bean
	public ShipmentPort shipmentPort(final ShipmentService service,
									 final PathFinderServicePort pathFinderServicePort,
									 final NotificationCreatorProvider notificationCreatorProvider,
									 final TrackingStatusServicePort trackingStatusServicePort,
									 final Set<ShipmentStatusHandler> shipmentStatusHandlers,
									 final CountryDetermineService countryDetermineService,
									 final PriceService priceService,
									 final CountryServiceAvailabilityService countryServiceAvailabilityService,
									 final SignatureService signatureService) {
		return new ShipmentPortImpl(service, LOGGER_FACTORY.getLogger(ShipmentPortImpl.class), pathFinderServicePort,
				notificationCreatorProvider, trackingStatusServicePort, shipmentStatusHandlers,
				countryDetermineService, priceService, countryServiceAvailabilityService, signatureService);
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
	@ConditionalOnProperty(name = "services.mock", havingValue = "true")
	public SignatureRepository signatureMockRepository() {
		return new SignatureMockRepositoryImpl();
	}

	@Bean
	public Set<ShipmentStatusHandler> shipmentStatusHandlers(final ShipmentService service) {
		return Set.of(new ShipmentCreatedHandler(), new ShipmentRerouteHandler(service),
				new ShipmentSentHandler(service), new ShipmentDeliveryHandler(service),
				new ShipmentRedirectHandler(service), new ShipmentReturnHandler(service));
	}

	@Bean("shipment.softwareConfigurationServicePort")
	@ConditionalOnProperty(name = "services.mock", havingValue = "false")
	public SoftwareConfigurationServicePort softwareConfigurationServicePort() {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using software configuration");
		final RetryConfig config = RetryConfig.custom()
				.maxAttempts(4)
				.waitDuration(Duration.ofSeconds(2))
				.retryExceptions(RuntimeException.class)
				.writableStackTraceEnabled(true)
				.build();
		return new SoftwareConfigurationServiceAdapter(config, softwareConfigurationProperties());
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true")
	public SoftwareConfigurationServicePort softwareConfigurationServiceMockPort() {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using mock software configuration");
		return new SoftwareConfigurationServiceMockAdapter();
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true")
	public RouteLogServicePort routeLogServiceMockPort() {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using mock Route log service port");
		return new RouteLogServiceMockAdapter();
	}

	@Bean(name = "shipment.routeLogServicePort")
	@ConditionalOnProperty(name = "services.mock", havingValue = "false")
	public RouteLogServicePort routeLogServicePort() {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Route log service port");
		final RetryConfig config = RetryConfig.custom()
				.maxAttempts(3)
				.waitDuration(Duration.ofSeconds(2))
				.retryExceptions(RuntimeException.class)
				.writableStackTraceEnabled(true)
				.build();
		return new RouteLogServiceAdapter(config);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "false")
	public ShipmentRepository shipmentRepository(final ShipmentReadRepository repository) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Shipment repository");
		return new ShipmentRepositoryImpl(repository);
	}

	@Bean
	@ConditionalOnProperty(name = "services.mock", havingValue = "true")
	public ShipmentRepository shipmentMockRepository() {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using Shipment mock repository");
		return new ShipmentRepositoryMockImpl();
	}
	
	@Bean("shipment.rerouteTokenServicePort")
	public TrackingStatusServicePort rerouteTokenServicePort(
			final TrackingStatusEventPublisher trackingStatusEventPublisher) {
		return new TrackingStatusServiceAdapter(trackingStatusEventPublisher);
	}

	@Bean
	public SoftwareConfigurationProperties softwareConfigurationProperties() {
		return new SoftwareConfigurationProperties();
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
										   final RouteLogServicePort routeLogServicePort,
										   final SoftwareConfigurationServicePort softwareConfigurationServicePort) {
		return new ShipmentServiceImpl(shipmentRepository, routeLogServicePort, softwareConfigurationServicePort);
	}

	@Bean("shipment.routeTrackerLogProperties")
	public RouteTrackerLogProperties routeTrackerLogProperties() {
		return new RouteTrackerLogProperties();
	}

	@Bean
	@ConditionalOnProperty(name="service.mock", havingValue="false")
	public PathFinderServicePort pathFinderServicePort(final VoronoiService voronoiService) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using path finder service");
		return new PathFinderAdapter(voronoiService);
	}

	//MOCK
	@Bean
	@ConditionalOnProperty(name="service.mock", havingValue="true")
	public PathFinderServicePort pathFinderMockServicePort(final PathFinderMockService pathFinderMockService) {
		LOGGER_FACTORY.getLogger(ShipmentConfiguration.class).warn("Using mock path finder service");
		return new PathFinderMockAdapter(pathFinderMockService);
	}
}
