package com.warehouse.shipment.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePort;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.NotificationCreatorProviderImpl;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import com.warehouse.voronoi.VoronoiService;

@Configuration
public class ShipmentConfiguration {

	private final LoggerFactory LOGGER_FACTORY = new LoggerFactoryImpl();

	@Bean
	public RouteLogAdapter shipmentAdapter(RouteLogEventPublisher routeLogEventPublisher) {
		final ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
		return new RouteLogAdapter(shipmentMapper, routeLogEventPublisher);
	}

	@Bean
	public ShipmentRepository shipmentRepository(ShipmentReadRepository repository) {
		final ParcelMapper parcelMapper = Mappers.getMapper(ParcelMapper.class);
		return new ShipmentRepositoryImpl(repository, parcelMapper);
	}

	@Bean(name = "shipment.mailPort")
	public MailPort mailPort(com.warehouse.mail.domain.service.MailService mailService) {
		return new MailPortImpl(mailService);
	}

	@Bean
	public NotificationCreatorProvider notificationCreatorService() {
		return new NotificationCreatorProviderImpl();
	}

	@Bean
	public ShipmentPort shipmentPort(ShipmentService service) {
		return new ShipmentPortImpl(service,
				LOGGER_FACTORY.getLogger(ShipmentPortImpl.class));
	}

	@Bean
	public ShipmentRequestMapper shipmentRequestMapper() {
		return Mappers.getMapper(ShipmentRequestMapper.class);
	}

	@Bean
	public ShipmentResponseMapper shipmentResponseMapper() {
		return Mappers.getMapper(ShipmentResponseMapper.class);
	}

	@Bean(name = "shipment.shipmentService")
	public ShipmentService shipmentService(ShipmentServicePort shipmentServicePort,
			PathFinderServicePort pathFinderServicePort, PaypalServicePort paypalServicePort,
			NotificationCreatorProvider notificationCreatorProvider, MailServicePort mailServicePort,
			ShipmentRepository shipmentRepository) {
		return new ShipmentServiceImpl(shipmentServicePort, shipmentRepository, pathFinderServicePort,
				paypalServicePort, notificationCreatorProvider, mailServicePort,
				LOGGER_FACTORY.getLogger(ShipmentServiceImpl.class));
	}

	@Bean
	public PaypalServicePort paypalServicePort(PaypalPort paypalPort) {
		final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
		return new PaypalAdapter(paypalPort, paymentMapper);
	}

	@Bean(name = "shipment.mailServicePort")
	public MailServicePort mailServicePort(MailPort mailPort) {
		final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
		return new MailAdapter(mailPort, notificationMapper);
	}

	@Bean
	public ShipmentReroutePort shipmentReroutePort(ShipmentService service) {
		return new ShipmentReroutePortImpl(service);
	}

	@Bean
	@ConditionalOnProperty(name="service.mock", havingValue="false")
	public PathFinderServicePort pathFinderServicePort(VoronoiService voronoiService) {
		return new PathFinderAdapter(voronoiService);
	}

	//MOCK
	@Bean
	@ConditionalOnProperty(name="service.mock", havingValue="true")
	public PathFinderServicePort pathFinderMockServicePort(PathFinderMockService pathFinderMockService) {
		return new PathFinderMockAdapter(pathFinderMockService);
	}
}
