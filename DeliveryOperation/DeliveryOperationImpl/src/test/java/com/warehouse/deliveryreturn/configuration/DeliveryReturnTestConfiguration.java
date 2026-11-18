package com.warehouse.deliveryreturn.configuration;

import com.warehouse.process.ProcessHubEventPublisher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentRepositoryServicePort;
import com.warehouse.tools.supplier.SupplierValidatorProperties;

@ComponentScan(basePackages = { "com.warehouse.deliveryreturn",  "com.warehouse.tools.parcelstatus" })
@EntityScan(basePackages = { "com.warehouse.deliveryreturn" })
@EnableJpaRepositories(basePackages = { "com.warehouse.deliveryreturn" })
@EnableAutoConfiguration
public class DeliveryReturnTestConfiguration {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }

    @MockitoBean
    public ShipmentRepositoryServicePort shipmentRepositoryServicePort;

    @MockitoBean
    public MailServicePort mailServicePort;

    @MockitoBean
    public SupplierValidatorProperties supplierValidatorProperties;

    @MockitoBean
    public ProcessHubEventPublisher processHubEventPublisher;
}
