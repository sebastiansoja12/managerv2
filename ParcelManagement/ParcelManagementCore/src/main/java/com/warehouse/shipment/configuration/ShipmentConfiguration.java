package com.warehouse.shipment.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.depot.api.DepotService;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.PaypalServicePort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.ShipmentServicePort;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import com.warehouse.voronoi.VoronoiService;

@Configuration
public class ShipmentConfiguration {

	@Bean
	public ShipmentServiceAdapter shipmentAdapter(RouteLogEventPublisher routeLogEventPublisher) {
		final ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
		return new ShipmentServiceAdapter(shipmentMapper, routeLogEventPublisher);
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
		return new ShipmentPortImpl(service);
	}

	@Bean(name = "shipment.shipmentService")
	public ShipmentService shipmentService(ShipmentServicePort shipmentServicePort,
			PathFinderServicePort pathFinderServicePort, PaypalServicePort paypalServicePort,
			NotificationCreatorProvider notificationCreatorProvider, MailServicePort mailServicePort,
			ShipmentRepository shipmentRepository) {
		return new ShipmentServiceImpl(shipmentServicePort, shipmentRepository, pathFinderServicePort,
				paypalServicePort, notificationCreatorProvider, mailServicePort);
	}

	@Bean
	public PathFinderServicePort pathFinderServicePort(VoronoiService voronoiService, DepotService depotService) {
		return new PathFinderAdapter(voronoiService, depotService);
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
}
