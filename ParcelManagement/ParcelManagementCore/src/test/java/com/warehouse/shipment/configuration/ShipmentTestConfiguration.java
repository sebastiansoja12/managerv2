package com.warehouse.shipment.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentController;
import com.warehouse.voronoi.VoronoiService;

@ComponentScan(basePackages = { "com.warehouse.shipment"})
@EntityScan(basePackages = { "com.warehouse.shipment"})
@EnableJpaRepositories(basePackages = { "com.warehouse.shipment"})
public class ShipmentTestConfiguration {

    @MockBean
    public JavaMailSender javaMailSender;

    @MockBean
    public MailService mailService;

    @MockBean
    public ShipmentController shipmentController;

    @MockBean
    public DepotPort depotPort;

    @MockBean
    public VoronoiService voronoiService;

    @MockBean
    public RouteLogServicePort routeLogServicePort;

}
