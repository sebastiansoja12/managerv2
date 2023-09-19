package com.warehouse.paypal.configuration;

import com.paypal.base.rest.APIContext;
import com.warehouse.paypal.domain.model.RedirectUrls;
import com.warehouse.paypal.domain.properties.PaypalConfigurationProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.paypal" })
@EntityScan(basePackages = { "com.warehouse.paypal" })
@EnableJpaRepositories(basePackages = { "com.warehouse.paypal" })
public class PaypalTestConfiguration {

    @MockBean
    public PaypalConfigurationProperties paypalConfigurationProperties;

    @MockBean
    public APIContext apiContext;

    @MockBean
    public RedirectUrls redirectUrls;
}
