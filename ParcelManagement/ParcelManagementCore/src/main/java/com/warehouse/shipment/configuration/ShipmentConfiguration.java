package com.warehouse.shipment.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.domain.port.primary.*;
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
	public ShipmentAdapter shipmentAdapter(RouteLogEventPublisher routeLogEventPublisher) {
		final ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
		return new ShipmentAdapter(shipmentMapper, routeLogEventPublisher);
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

	@Bean(name = "shipment.pathFinderServicePort")
	public PathFinderServicePort pathFinderServicePort(VoronoiService voronoiService) {
		return new PathFinderAdapter(voronoiService);
	}

	@Bean
	public PaypalServicePort paypalServicePort(PaypalPort paypalPort) {
		final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
		return new PaypalAdapter(paypalPort, paymentMapper);
	}

	@Bean
	public MailServicePort mailServicePort(MailService mailService) {
		final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
		return new MailAdapter(mailService, notificationMapper);
	}

	@Bean
	public ShipmentReroutePort shipmentReroutePort(ShipmentService service) {
		return new ShipmentReroutePortImpl(service);
	}

	@Bean
	public ShipmentRestPort shipmentRestPort(ShipmentService service) {
		return new ShipmentRestPortImpl(service, LOGGER_FACTORY.getLogger(ShipmentRestPortImpl.class));
	}
}
