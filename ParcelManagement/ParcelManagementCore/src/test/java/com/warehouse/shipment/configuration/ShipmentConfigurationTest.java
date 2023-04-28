package com.warehouse.shipment.configuration;

import com.warehouse.voronoi.VoronoiService;
import com.warehouse.depot.api.DepotService;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentController;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

@ComponentScan(basePackages = { "com.warehouse.shipment" })
@EntityScan(basePackages = { "com.warehouse.shipment" })
@EnableJpaRepositories(basePackages = { "com.warehouse.shipment" })
@Configuration
public class ShipmentConfigurationTest {

    @MockBean
    public JavaMailSender javaMailSender;

    @MockBean
    public MailService mailService;

    @MockBean
    public PaypalPort paypalPort;

    @MockBean
    public ShipmentController shipmentController;

    @MockBean
    public DepotService depotService;

    @MockBean
    public VoronoiService voronoiService;

    @MockBean
    public RouteLogEventPublisher routeLogEventPublisher;

}
