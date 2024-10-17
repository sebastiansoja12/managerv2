package com.warehouse.shipment.configuration;

import java.time.Duration;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.NotificationCreatorProviderImpl;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidatorImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.voronoi.VoronoiService;

import io.github.resilience4j.retry.RetryConfig;

@Configuration
public class ShipmentConfiguration {

	private final LoggerFactory LOGGER_FACTORY = new LoggerFactoryImpl();

	@Bean
	public ShipmentRepository shipmentRepository(final ShipmentReadRepository repository) {
		return new ShipmentRepositoryImpl(repository);
	}

	@Bean(name = "shipment.mailPort")
	public MailPort mailPort(final com.warehouse.mail.domain.service.MailService mailService) {
		return new MailPortImpl(mailService);
	}

	@Bean
	public NotificationCreatorProvider notificationCreatorService() {
		return new NotificationCreatorProviderImpl();
	}

	@Bean
	public ShipmentPort shipmentPort(final ShipmentService service,
									 final PathFinderServicePort pathFinderServicePort,
									 final NotificationCreatorProvider notificationCreatorProvider,
									 final MailServicePort mailServicePort) {
		return new ShipmentPortImpl(service, LOGGER_FACTORY.getLogger(ShipmentPortImpl.class), pathFinderServicePort,
				notificationCreatorProvider, mailServicePort);
	}

	@Bean
	public SoftwareConfigurationServicePort softwareConfigurationServicePort() {
		final RetryConfig config = RetryConfig.custom()
				.maxAttempts(4)
				.waitDuration(Duration.ofSeconds(2))
				.retryExceptions(RuntimeException.class)
				.writableStackTraceEnabled(true)
				.build();
		return new SoftwareConfigurationServiceAdapter(config);
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
	public ShipmentRequestValidator shipmentRequestValidator() {
		return new ShipmentRequestValidatorImpl();
	}

	@Bean(name = "shipment.shipmentService")
	public ShipmentService shipmentService(final ShipmentRepository shipmentRepository,
										   final RouteLogServicePort routeLogServicePort,
										   final SoftwareConfigurationServicePort softwareConfigurationServicePort) {
		return new ShipmentServiceImpl(shipmentRepository, routeLogServicePort, softwareConfigurationServicePort);
	}

	@Bean(name = "shipment.routeLogServicePort")
	public RouteLogServicePort routeLogServicePort() {
		final RetryConfig config = RetryConfig.custom()
				.maxAttempts(3)
				.waitDuration(Duration.ofSeconds(2))
				.retryExceptions(RuntimeException.class)
				.writableStackTraceEnabled(true)
				.build();
		return new RouteLogServiceAdapter(config);
	}

	@Bean("shipment.routeTrackerLogProperties")
	public RouteTrackerLogProperties routeTrackerLogProperties() {
		return new RouteTrackerLogProperties();
	}
	
	@Bean(name = "shipment.mailServicePort")
	public MailServicePort mailServicePort(final MailPort mailPort) {
		final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
		return new MailAdapter(mailPort, notificationMapper);
	}

	@Bean
	@ConditionalOnProperty(name="service.mock", havingValue="false")
	public PathFinderServicePort pathFinderServicePort(final VoronoiService voronoiService) {
		return new PathFinderAdapter(voronoiService);
	}

	//MOCK
	@Bean
	@ConditionalOnProperty(name="service.mock", havingValue="true")
	public PathFinderServicePort pathFinderMockServicePort(final PathFinderMockService pathFinderMockService) {
		return new PathFinderMockAdapter(pathFinderMockService);
	}
}
