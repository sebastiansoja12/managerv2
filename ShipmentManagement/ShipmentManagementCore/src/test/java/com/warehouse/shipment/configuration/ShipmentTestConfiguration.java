package com.warehouse.shipment.configuration;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentInternalController;
import com.warehouse.shipment.infrastructure.adapter.secondary.ExternalFeignClient;
import com.warehouse.tools.returning.ReturnProperties;
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
    public ShipmentInternalController shipmentInternalController;

    @MockBean
    public DepartmentPort departmentPort;

    @MockBean
    public VoronoiService voronoiService;

    @MockBean
    public RouteLogServicePort routeLogServicePort;

    @Bean
    ExternalFeignClient externalMicroserviceFeignClient() {
        return Mockito.mock(ExternalFeignClient.class);
    }

    @Bean
    ReturnProperties returnProperties() {
        final ReturnProperties returnProperties = new ReturnProperties();
        returnProperties.setUrl("http://localhost:8070");
        returnProperties.setEndpoint("/v2/api/returns");
        return returnProperties;
    }

}
