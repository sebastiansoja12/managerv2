package com.warehouse.routetracker.configuration;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.routetracker", "com.warehouse.depot"})
@EntityScan(basePackages = { "com.warehouse.routetracker", "com.warehouse.depot"})
@EnableJpaRepositories(basePackages = { "com.warehouse.routetracker", "com.warehouse.depot"})
@PropertySource("classpath:application-hsqlmem.properties")
public class RouteTrackerTestConfiguration {

    @MockBean
    public AuthenticationPort authenticationPort;

    @MockBean
    public ShipmentPort shipmentPort;

    @MockBean
    public MailService mailService;

    @MockBean
    public PaypalPort port;

}
