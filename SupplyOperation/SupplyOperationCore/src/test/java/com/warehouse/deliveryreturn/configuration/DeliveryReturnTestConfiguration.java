package com.warehouse.deliveryreturn.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;

@ComponentScan(basePackages = { "com.warehouse.deliveryreturn",  "com.warehouse.tools.parcelstatus" })
@EntityScan(basePackages = { "com.warehouse.deliveryreturn" })
@EnableJpaRepositories(basePackages = { "com.warehouse.deliveryreturn" })
@EnableAutoConfiguration
public class DeliveryReturnTestConfiguration {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }

    @MockBean
    public RouteLogServicePort routeLogServicePort;

    @MockBean
    public ParcelRepositoryServicePort parcelRepositoryServicePort;

    @MockBean
    public MailServicePort mailServicePort;
}
