package com.warehouse.redirect.configuration;

import com.warehouse.shipment.infrastructure.api.ShipmentService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

@ComponentScan(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
@EntityScan(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
@EnableJpaRepositories(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
public class RedirectTokenTestConfiguration {

    @MockBean
    public JavaMailSender javaMailSender;

    @MockBean
    public ShipmentService service;
}
