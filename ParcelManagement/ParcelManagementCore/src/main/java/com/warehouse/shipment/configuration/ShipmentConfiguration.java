package com.warehouse.shipment.configuration;

import com.warehouse.depot.api.DepotService;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.service.NotificationCreatorService;
import com.warehouse.shipment.domain.service.NotificationCreatorServiceImpl;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentRepositoryImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShipmentConfiguration {

    @Bean
    public ShipmentAdapter shipmentAdapter(ShipmentRepository shipmentRepository, MailPort mailPort,
        PaypalPort paypalPort, NotificationCreatorService creatorService,
        RouteLogEventPublisher routeLogEventPublisher, VoronoiService voronoiService, DepotService depotService) {
        final ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
        final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
        return new ShipmentAdapter(shipmentMapper, shipmentRepository, mailPort, notificationMapper, paypalPort,
                creatorService, routeLogEventPublisher, voronoiService, depotService);
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
    public NotificationCreatorService notificationCreatorService() {
        return new NotificationCreatorServiceImpl();
    }

    @Bean
    public ShipmentPort shipmentPort(ShipmentService service) {
        return new ShipmentPortImpl(service);
    }

    @Bean(name = "shipment.shipmentService")
    public ShipmentService shipmentService(com.warehouse.shipment.domain.port.secondary.ShipmentPort shipmentPort,
                                           ShipmentRepository shipmentRepository) {
        return new ShipmentServiceImpl(shipmentPort, shipmentRepository);
    }
}
