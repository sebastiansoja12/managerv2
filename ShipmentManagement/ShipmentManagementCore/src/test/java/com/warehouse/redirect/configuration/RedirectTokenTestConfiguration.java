package com.warehouse.redirect.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;

@ComponentScan(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
@EntityScan(basePackages = { "com.warehouse.redirect", "com.warehouse.mail", "com.warehouse.shipment" })
@EnableJpaRepositories(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
public class RedirectTokenTestConfiguration {

    @MockBean
    public JavaMailSender javaMailSender;

    @MockBean
    public MailPort mailPort;

    @MockBean
    public MailServicePort mailServicePort;
}
