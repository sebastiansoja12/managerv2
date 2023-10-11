package com.warehouse.reroute.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.reroute.domain.port.secondary.MailServicePort;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.voronoi.VoronoiService;

@ComponentScan(basePackages = { "com.warehouse.reroute", "com.warehouse.mail" })
@EntityScan(basePackages = { "com.warehouse.reroute", "com.warehouse.mail" })
@EnableJpaRepositories(basePackages = { "com.warehouse.reroute", "com.warehouse.mail" })
public class RerouteTokenTestConfiguration {

    @MockBean
    public JavaMailSender javaMailSender;

    @MockBean
    public ShipmentPort shipmentPort;

    @MockBean
    public ShipmentService shipmentService;

    @MockBean
    public VoronoiService voronoiService;

    @MockBean
    public DepotPort depotPort;

    @MockBean
    public MailServicePort mailServicePort;
}
